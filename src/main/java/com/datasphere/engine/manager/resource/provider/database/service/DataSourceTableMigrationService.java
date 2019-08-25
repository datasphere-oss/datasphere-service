package com.datasphere.engine.manager.resource.provider.database.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.engine.manager.resource.provider.database.model.DBQuery;
import com.datasphere.engine.manager.resource.provider.database.model.DBTableField;
import com.datasphere.engine.manager.resource.provider.database.service.impl.DataSourcePlatformServiceImpl;
import com.datasphere.engine.manager.resource.provider.database.service.impl.DataSourceWebSocketServiceImpl;
import com.datasphere.engine.manager.resource.provider.model.DataSource;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class DataSourceTableMigrationService {

	private static final Log logger = LogFactory.getLog(DataSourceTableMigrationService.class);

	private final Map<String, Result> migrateResultMap = new ConcurrentHashMap<>();

	private final Map<String, Boolean> controlMap = new ConcurrentHashMap<>();

	private final ExecutorService migrationTheadPool;

	private volatile int migrateDataSegment = 10;

	@Autowired
	public DataSourcePlatformServiceImpl platformAccessService;

	@Resource(name="dataSourceConsoleService")
    DataSourceConsoleService consoleService;

	@Autowired
    DataSourceWebSocketServiceImpl webSocketService;

	DataSourceTableMigrationService(){
		migrationTheadPool = Executors.newCachedThreadPool();
    }

	public void setDataSegment(int value){
		migrateDataSegment = value;
	}

	public void shutdown(){
		migrationTheadPool.shutdown();
		while (!migrationTheadPool.isTerminated()) {
            Thread.yield();
        }
	}

	public void shutdown(String tid){
		if(controlMap.containsKey(tid)){
			logger.info("DS_DB : ["+tid+"] task shutdown.");
			controlMap.put(tid, false);
		}
	}

	public void remove(String tid){

		if(controlMap.containsKey(tid)){
			logger.info("DS_DB : ["+tid+"] task control remove.");
			controlMap.remove(tid);
		}

		if(migrateResultMap.containsKey(tid)){
			migrateResultMap.remove(tid);
			logger.info("DS_DB : ["+tid+"] task remove.");
		}
	}

	public boolean exsits(String tid){
		return controlMap.containsKey(tid);
	}

	public synchronized void migrate(String tid,Attachment attachment){
		if(!migrateResultMap.containsKey(tid)){
			Result result = new Result();
			result.setState(TaskState.INIT.getState());
			result.setMessage(TaskState.INIT.getMessage());
			migrateResultMap.put(tid,result);
			controlMap.put(tid, true);

			MigrationTask task = new MigrationTask();
			task.setTaskId(tid);
			task.setDataSegment(migrateDataSegment);
			task.setAttachment(attachment);
			task.setResult(result);
			migrationTheadPool.execute(task);
			logger.info("DS_DB : ["+tid+"] task migration start.");
		}
	}

	public Result getResult(String tid){
		return migrateResultMap.get(tid);
	}

	public class MigrationTask implements Runnable {

		private Attachment attachment;

		private int dataSegment;

		private String taskId;

		private Result result;

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public void setDataSegment(int dataSegment) {
			this.dataSegment = dataSegment;
		}

		public void setAttachment(Attachment attachment) {
			this.attachment = attachment;
		}

		public void setResult(Result result) {
			this.result = result;
		}

		private void updateDataSource(){
			//更新工作台数据源表信息
			DataSource dataSource = new DataSource();
			dataSource.setLastModified(new Date());
			dataSource.setDataState(result.getState());
			dataSource.setDataSize(result.getSize());
			dataSource.setDataMessage(result.getMessage());
			dataSource.setId(taskId);
			try{
//				consoleService.update(dataSource);//todo
				logger.info("DS_DB : ["+taskId+"] update datasource success");
			}catch(Exception e){
				e.printStackTrace();
				logger.error("DS_DB_ERR : ["+taskId+"] warnning because of update datasource failed.");
			}

		}

		private void webSocketNotice(int type){
			try {
//				if(type == 1)
//					webSocketService.afterComplete(taskId,attachment.getUserContext());
//				else
//					webSocketService.onTransmitting(taskId,attachment.getUserContext());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("DS_DB_ERR : ["+taskId+"] warnning because of call websocket noice failed.");
			}
		}

		private Long getDataSourceSizeFromDal(){
			Long dataSize = null;
			try {
				dataSize = platformAccessService.readSize(taskId);
				logger.info("DS_DB : task is ["+taskId+"] ");
			} catch (Exception e) {
				e.printStackTrace();
				dataSize = -1L;
				logger.error("DS_DB_ERR : ["+taskId+"] read datasource size failed.");
			}
			return dataSize;
		}

		private boolean putDataToDal(List<List<DBTableField>> segmentList){
			try{
				platformAccessService.add(taskId, segmentList);
				logger.info("DS_DB : wirte data number ["+segmentList.size()+"] ");
				return true;
			}catch(Exception e){
				e.printStackTrace();
				logger.error("DS_DB_ERR : ["+taskId+"] add data to dal failed.");
				return false;
			}
		}

		private boolean createTableToDal(List<List<DBTableField>> dataList){
			try{
				platformAccessService.write(taskId,"",dataList);
				logger.info("DS_DB : wirte data number ["+dataList.size()+"] ");
				return true;
			}catch(Exception e){
				e.printStackTrace();
				logger.error("DS_DB_ERR : ["+taskId+"] create table and write data to dal failed.");
				return false;
			}
		}

		private List<List<DBTableField>> getDataFromDB(){
			List<List<DBTableField>> dataList = null;
			try{
				dataList = attachment.getDatabaseService().readTable(attachment.getDbQuery());
				logger.info("DS_DB : get data total number ["+dataList.size()+"] ");
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("DS_DB_ERR : ["+taskId+"] read data from database failed.");
			}
			return dataList;
		}

		private void returnComplete(TaskState state){
			logger.info("DS_DB : ["+taskId+"] task complete!");
			result.setState(state.getState());
			result.setMessage(state.getMessage());
			updateDataSource();
			webSocketNotice(1);
			remove(taskId);

		}

		private void returnFailure(TaskState state){
			logger.info("DS_DB : ["+taskId+"] task failure!");
			result.setState(state.getState());
			result.setMessage(state.getMessage());
			updateDataSource();
			webSocketNotice(1);
			remove(taskId);
		}

		private void returnInterrupt(){
			logger.info("DS_DB : ["+taskId+"] task interrupt!");
			result.setState(TaskState.INTERRUPT.getState());
			result.setMessage(TaskState.INTERRUPT.getMessage());
			updateDataSource();
			webSocketNotice(1);
			remove(taskId);
		}

		@Override
		public void run() {
			//读取源数据表数据
			List<List<DBTableField>> dataList = getDataFromDB();
			if(dataList == null || dataList.size() == 0){
				returnFailure(TaskState.READ_FAILED);
				return ;
			}

			int totalNumber = dataList.size();
			result.setTotalNumber(totalNumber);

			//现在dal中创建表
			if(!createTableToDal(dataList)){
				returnFailure(TaskState.CREATE_FAILED);
				return ;
			}

			//将状态更新为传输中。
			logger.info("DS_DB : ["+taskId+"] set state 3!");
			result.setState(TaskState.RUNNING.getState());
			result.setMessage(TaskState.RUNNING.getMessage());

			//计算数据分几次操作
			int insertCount = totalNumber%dataSegment == 0 ?
					totalNumber/dataSegment : totalNumber/dataSegment + 1;

			int successNumber = 0;


			//调用数据源平台 插入数据接口
			for(int i=0;i<insertCount;i++){
				//try {Thread.sleep(1000);} catch (InterruptedException e1) {}

				//如果任务被删除，或者执行任务状态为false,则中断任务
				if(!exsits(taskId) || !controlMap.get(taskId)){
					returnInterrupt();
					return ;
				}

				//100条件数据，每次插入10条 toIndex=10,如果101条数据 toIndex = 11
				int lastIndex = totalNumber - dataSegment * i;
				int toIndex = lastIndex < dataSegment ? lastIndex : dataSegment;
				List<List<DBTableField>> segmentList = dataList.subList(0, toIndex);
				if(putDataToDal(segmentList)){
					successNumber += segmentList.size();
					result.setSuccessNumber(successNumber);
				}
				segmentList.clear();

				//推送当前任务执行每批数据插入的结果
				webSocketNotice(0);
			}

			result.setSize(getDataSourceSizeFromDal());
			//设置任务执行结果
			if(totalNumber == successNumber){
				returnComplete(TaskState.FINISHED);
			}else{
				logger.info("DS_DB : ["+taskId+"] ["+result+"]");
				returnFailure(TaskState.INCOMPLETE);
			}

		}
	}

	public static class Attachment{
//		private UserContext userContext;
		private DBQuery dbQuery;
		private DataSourceDatabaseService databaseService;


//		public UserContext getUserContext() {
//			return userContext;
//		}
//		public void setUserContext(UserContext userContext) {
//			this.userContext = userContext;
//		}
		public DBQuery getDbQuery() {
			return dbQuery;
		}
		public void setDbQuery(DBQuery dbQuery) {
			this.dbQuery = dbQuery;
		}
		public DataSourceDatabaseService getDatabaseService() {
			return databaseService;
		}
		public void setDatabaseService(DataSourceDatabaseService databaseService) {
			this.databaseService = databaseService;
		}
	}

	// 0 初始状态  1 任务完成 2 读取数据失败 3 数据传输中 4 数据传输不完整 5任务中断
	public enum TaskState {
		INIT(0,"任务初始化"),
		FINISHED(1, "任务完成"),
		READ_FAILED(2, "任务失败，读取失败"),
		RUNNING(3, "任务执行中"),
		INCOMPLETE(4,"任务失败，数据导入不完整"),
		INTERRUPT(5,"任务失败，任务被中断"),
		CREATE_FAILED(6, "任务失败，创建失败");
	    private String message ;
	    private int state ;

	    private TaskState(Integer state,String message){
	        this.state = state ;
	        this.message = message ;
	    }

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}
	}

	public class Result{
		// 0 初始状态  1 任务完成 2 读取数据失败 3 数据传输中 4 数据传输不完整 5任务中断
		private volatile int state ;
		private volatile int totalNumber;
		private volatile int successNumber;
		private volatile String message;
		private volatile long size;

		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public int getTotalNumber() {
			return totalNumber;
		}
		public void setTotalNumber(int totalNumber) {
			this.totalNumber = totalNumber;
		}
		public int getSuccessNumber() {
			return successNumber;
		}
		public void setSuccessNumber(int successNumber) {
			this.successNumber = successNumber;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		@Override
		public String toString() {
			return "Result [state=" + state + ", totalNumber=" + totalNumber + ", successNumber=" + successNumber
					+ ", message=" + message + ", size=" + size + "]";
		}

	}
}
