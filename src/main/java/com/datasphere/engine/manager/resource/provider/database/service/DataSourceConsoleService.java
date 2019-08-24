package com.datasphere.engine.manager.resource.provider.database.service;

import com.datasphere.core.common.BaseService;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.manager.resource.provider.database.entity.DBCommonInfo;
import com.datasphere.engine.manager.resource.provider.database.entity.DBDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.database.entity.DBTableDataList;
import com.datasphere.engine.manager.resource.provider.database.service.impl.DataSourcePlatformServiceImpl;
import com.datasphere.engine.manager.resource.provider.database.util.BeanToMapUtil;
import com.datasphere.engine.manager.resource.provider.database.util.DALTypeUtil;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;
import com.datasphere.engine.manager.resource.provider.model.DataSource;
import com.datasphere.server.connections.utils.ObjectMapperUtils;
import com.datasphere.server.manager.common.utils.UUIDUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

@Singleton
public class DataSourceConsoleService extends BaseService {

	public static final Log logger = LogFactory.getLog(DataSourceConsoleService.class);
	
	@Inject
    DataSourceTableMigrationService migrationService;

//	@Inject
//	DataSourceService dataSourceService;
	
	@Inject
	public DataSourcePlatformServiceImpl platformAccessService;
	
	/**
	 * 补充验证
	 * @param info
	 */
	private void validate(DBCommonInfo info){
		String databaseName = info.getDatabaseName();
		String databaseType = info.getDatabaseType();
		String schemaName = info.getSchemaName();
		JAssert.isTrue(databaseType != null);
		switch(databaseType.toLowerCase()){
			//oracle 用两种连接方式，需要根据serviceType判断
			case "oracle" :
				JAssert.isTrue(!StringUtils.isBlank(info.getServiceType()),"ORACLE数据库缺失参数[serviceType]");
				JAssert.isTrue(!StringUtils.isBlank(info.getServiceName()),"ORACLE数据库缺失参数[serviceName]");
				break;
			//如果数据库类型为mysql或mssql是，需要传入数据库名称。用于 jdbc setCataLog
			case "mysql" : 
				JAssert.isTrue(!StringUtils.isBlank(databaseName),"提交参数错误，数据库名不能为空");
				break;
			case "mssql" :
				JAssert.isTrue(!StringUtils.isBlank(databaseName),"提交参数错误，数据库名不能为空");
				JAssert.isTrue(!StringUtils.isBlank(schemaName),"提交参数错误，模式名不能为空");
				break;
			default:
				JAssert.isTrue(false,"数据库类型不支持");
		}
	}
	
	/**
	 * 根据配置信息得到对应的service实现类
	 * 
	 * @param info
	 * @return
	 */
	private DataSourceDatabaseService getDatabaseService(DBCommonInfo info){
		 DataSourceDatabaseService databaseService = DataSourceDatabaseFactory.create(BeanToMapUtil.convertBean(info));
		 JAssert.isTrue(databaseService != null, "数据源配置错误无法操作");
		 return databaseService;
	}
	
	/**
	 * 将字段dataSourceJson中的json转换成对象，映射到dataSources对象中。
	 * 
	 * @param info
	 * @return
	 */
	public DBCommonInfo commonEntityWrapper(DBCommonInfo info){
		String dataSourceJson = info.getDataSourceJson();
		JAssert.isTrue(!StringUtils.isBlank(dataSourceJson), "参数错误");
		List<DBDataSourceInfo> dbDataSourceInfo = ObjectMapperUtils.fromListJson(dataSourceJson, DBDataSourceInfo.class);
		info.setDataSources(dbDataSourceInfo);
		return info;
	}
	
	/**
	 * 查询表信息，包括表名、表总记录数、表字段个数
	 * 
	 * @param info
	 * @return
	 */
	public List<DBTableInfodmp> listTableInfo(DBCommonInfo info) {
		validate(info);
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		List<DBTableInfodmp> infoList = databaseService.listTableInfo(info);
		//infoList 不会为null
		JAssert.isTrue(infoList.size() > 0, "没有发现数据库");
		return infoList;
	}
	
	/**
	 * 列出所有数据库名，如果有模式名(schema name)同时列出每个库下的所有模式
	 * 
	 * @param info
	 * @return
	 */
	public Map<String,String[]> listDatabase(DBCommonInfo info){
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		Map<String,String[]> dataMap = new HashMap<>();
		String[] databases = databaseService.listDatabase();
		for(String dbname : databases){
			//listSchema 返回值不会为null
			String[] schemas = databaseService.listSchema(dbname);
			dataMap.put(dbname,schemas);
		}
		return dataMap;
	}
	
	/**
	 * 分页查询表数据
	 * 
	 * @param info
	 * @return
	 */
	public DBTableDataList listTableDataWithPaging(DBCommonInfo info) {
		validate(info);
		JAssert.isTrue(!StringUtils.isBlank(info.getTableName()), "查询的数据表名不能为空");
		//判断该表是否存在
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		JAssert.isTrue(databaseService.tableExsit(info), "数据库表不存在");
		//默认显示100行
		if(info.getRows() == 0){
			info.setRows(100);
		}
		//默认显示页码为1
		if(info.getPage() == 0){
			info.setPage(1);
		}
		
		DBTableDataList dataEntity = new DBTableDataList();
		Map<String,Integer> columnTypeMap = new HashMap<>();
		List<Map<String, String>> data = databaseService.readTableWithColumnName(info,columnTypeMap);
		if(data.size() == 0){
			//读取数据为空直接返回
			return dataEntity;
		}
		
		//将表的字段分为，支持的字段和不支持的字段
		List<String> columnTypeList = new LinkedList<>();
		List<String> columnList = new LinkedList<>();
		List<String> unsupportColumnList = new LinkedList<>();
		for(Entry<String, Integer> entry : columnTypeMap.entrySet()){
			if(DALTypeUtil.isSupport(entry.getValue())){
				String columnType = DALTypeUtil.convertBusinessType(entry.getValue());
				columnList.add(entry.getKey());
				columnTypeList.add(columnType);
			}else{
				unsupportColumnList.add(entry.getKey());
			}
		}
		dataEntity.setTypeList(columnTypeList);
		dataEntity.setColumns(columnList);
		dataEntity.setUnsupportColumns(unsupportColumnList);
		
		//将支持的字段数据存放到DBTableDataList
		List<List<String>> dataList = new LinkedList<>();
		for(Map<String,String> col : data){
			List<String> rowList = new LinkedList<>();
			for(Entry<String, String> entry : col.entrySet()){
				if(columnList.contains(entry.getKey())){
					rowList.add(entry.getValue());
				}
			}
			dataList.add(rowList);
		}
		dataEntity.setDataList(dataList);
		
		return dataEntity;
	}
	
	/**
	 * 验证表字段类型是否支持
	 * 
	 * @param info
	 * @return
	 */
	public Map<String,List<String>> validTableColumnType(DBCommonInfo info){
		validate(info);
		List<DBDataSourceInfo> dataSources = info.getDataSources();
		JAssert.isTrue(dataSources != null, "提交参数不完整");
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		List<String> tables = new LinkedList<>();
		for(DBDataSourceInfo dsinfo : dataSources){
			JAssert.isTrue(!StringUtils.isEmpty(dsinfo.getTableName()), "验证表名不能为空");
			tables.add(dsinfo.getTableName());
		}
		return databaseService.getUnsupportTableColumn(info,tables);
	}
	
	/**
	 * 提交的数据源列表是否有重复的数据源名称
	 * 
	 * @param infoList
	 * @return
	 */
	public boolean hasRepeatDataSourceName(List<DBDataSourceInfo> infoList){
		HashMap<String,String> map = new HashMap<>();
		for(DBDataSourceInfo info : infoList){
			String name = info.getDatasourceName();
			if(map.containsKey(name)){
				return true;
			}else{
				map.put(name, null);
			}
		}
		return false;
		
	}
	
	/**
	 * 数据源名称在app_source表中是否存在
	 * 
	 * @param info
	 * @return
	 */
	public String[] exsitDataSourceName(DBCommonInfo info){
		List<DBDataSourceInfo> dataSources = info.getDataSources();
		JAssert.isTrue(dataSources != null, "提交参数不完整");
		JAssert.isTrue(!hasRepeatDataSourceName(dataSources), "数据源名称重复");
//		String userId = getUserContext().getOmSysUser().getUserId();
		List<String> nameList = null;  // TODO 改动 baseDao.listAllDataSourceName(userId);
		List<String> repeatDSNameList = new LinkedList<>();
		for(DBDataSourceInfo dsinfo : dataSources){
			if(nameList != null && nameList.size() > 0){
				for(String name : nameList){
					if(name.equals(dsinfo.getDatasourceName())){
						repeatDSNameList.add(dsinfo.getDatasourceName());
						break;
					}
				}
			}
		}
		return repeatDSNameList.toArray(new String[repeatDSNameList.size()]);
	}
	
	public Map<String,Integer> getUnsupportColumns(Map<String,Integer> columnMap){
		Map<String,Integer> unsupportColMap = new HashMap<>(); 
		String[] words  = columnMap.keySet().toArray(new String[columnMap.size()]);
		//{col1:1,col2:2} 传map，状态位1是关键字，2是命名规则
		Map<String, Integer> unsupportColumnNameMap = platformAccessService.check(words);
		
		//columnMap key存放字段名称  value存放字段的JDBC类型
		for(Entry<String, Integer> entry : columnMap.entrySet()){
			//2 数据表中存在不支持的字段名称(名字为关键字或命名规则不符)
			 if(unsupportColumnNameMap.containsKey(entry.getKey())){
				unsupportColMap.put(entry.getKey(), 2);
			 }
		}
		
		//如果字段名称不支持，先提示
		if(unsupportColMap.size() > 0){
			return unsupportColMap;
		}
		
		
		//检查是否所有字段类型都不支持
		for(Entry<String, Integer> entry : columnMap.entrySet()){
			//1 数据表中存在不支持的字段类型  
			if(!DALTypeUtil.isSupport(entry.getValue())){
				unsupportColMap.put(entry.getKey(), 1);
			}
		}
		
		//只要存在支持的字段，就满足验证。因为调用根据size判断，所以此处清空unsupportColMap
		if(unsupportColMap.size() != columnMap.size()){
			unsupportColMap.clear();
		}
		
		return unsupportColMap;
	}
	
	/**
	 * 创建数据源，可以创建多个数据源，数据源个数等于用户选择表的个数。
	 * 每一数据源用一个线程处理，如果某个表存在不支持的字段，则返回一个该表所有不支持的字段名称和原因
	 * 
	 * @param info
	 * @return 
	 */
	public List<String> createDataSource(DBCommonInfo info,Map<String,Map<String,Integer>> unsupportTable){
		logger.info("DS_DB : start create-datasource task");
		List<Object[]> taskList = new LinkedList<>();
		List<DBDataSourceInfo> dataSources = info.getDataSources();
		JAssert.isTrue(dataSources != null, "提交参数不完整");
		JAssert.isTrue(unsupportTable != null, "方法参数不能为空");
//		UserContext userContext = getUserContext();
		for(DBDataSourceInfo dsinfo : dataSources){
			String id = UUIDUtils.random();
			logger.info("DS_DB : ["+id+"] execute create-datasource id");
			if(StringUtils.isEmpty(dsinfo.getTableName()) || StringUtils.isEmpty(dsinfo.getDatasourceName())){
				logger.error("DS_DB : execute create-datasource error ["+dsinfo+"]");
				continue;
			}
			
			//将列表中的tableName添加到任务Entity中
			String tableName = dsinfo.getTableName();
			info.setTableName(tableName);
			
			//此处需要复制DBCommonInfo 防止多线程共用一个实例
			DBCommonInfo cloneInfo = info.clone();
			DataSourceDatabaseService databaseService = getDatabaseService(cloneInfo);
			
			//查询表字段名和类型，存到app_datasource表中
			Map<String,Integer> columnMap = databaseService.getColumnsNameAndJdbcType(cloneInfo);
			
			//验证表字段类型和字段名是合法
			Map<String, Integer> unsupportColumnMap = getUnsupportColumns(columnMap);
			if(unsupportColumnMap.size() > 0){
				unsupportTable.put(tableName, unsupportColumnMap);
				logger.error("DS_DB : ["+id+"] has unsupport table column "+unsupportColumnMap.keySet() );
			}else{
				String dbConfigJson = ObjectMapperUtils.writeValue(cloneInfo);
				String columnJson = ObjectMapperUtils.writeValue(columnMap);
				
				DataSource dataSource = new DataSource();
				dataSource.setId(id);
				dataSource.setDataDesc(dsinfo.getDatasourceDesc());
				dataSource.setDataFileType(cloneInfo.getDatabaseType().toUpperCase());
				dataSource.setDataConfig(dbConfigJson);
				//0 文件数据源  1数据库数据源
				dataSource.setDataType(1);
				//0 表示数据源状态为初始状态
				dataSource.setDataState(0);
				dataSource.setDataSize(0L);
				//保存表字段属性信息
				dataSource.setDataHead(columnJson);
//				dataSource.setCreator(getUserContext().getOmSysUser().getUserId());//todo
				dataSource.setName(dsinfo.getDatasourceName());
				//001001 私有数据源 001002公共数据源
				dataSource.setClassification(dsinfo.getDatasourceClass());

				DataSourceTableMigrationService.Attachment attachment = new DataSourceTableMigrationService.Attachment();
//				attachment.setDbQuery(cloneInfo);//todo
//				attachment.setUserContext(userContext);//todo
				attachment.setDatabaseService(databaseService);
				taskList.add(new Object[]{id,dataSource,attachment});
			}
		}
		
		List<String> taskIds = new LinkedList<>();
		//不存在非法字段，则执行表迁移任务
		if(unsupportTable.size() == 0){
			for(Object[] objects: taskList){
				String id = (String)objects[0];
				DataSource dataSource = (DataSource)objects[1];
				DataSourceTableMigrationService.Attachment attachment = (DataSourceTableMigrationService.Attachment)objects[2];
				logger.info("DS_DB : ["+id+"] execute create-datasource insert.");
				//插入一条数据源数据，同时插入一条关联的组件描述数据
//				dataSourceService.createRelationComponentDefinition(dataSource);

				logger.info("DS_DB : execute create-datasource data migration task.");
				migrationService.migrate(id,attachment);
				taskIds.add(id);
			}
		}
		
		return taskIds;
	}
	
	/**
	 * 查看源表字段名或类型是否有变更，如果存在变更返回变更的所有字段名，否则返回空数组。
	 * 
	 * @param id
	 * @param info
	 * @return
	 */
	public String[] changedSourceTableColumn(String id,DBCommonInfo info){
		validate(info);
		JAssert.isTrue(!StringUtils.isEmpty(info.getTableName()), "表名不能为空");
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		DataSource ds = getDataSourceById(id,true);
		String headJson = ds.getDataHead();
		JAssert.isTrue(!StringUtils.isEmpty(headJson), "数据源字段信息读取错误");
		@SuppressWarnings("unchecked")
		Map<String,Integer> sourceColumnMap = ObjectMapperUtils.readValue(headJson, Map.class);
		Map<String,Integer> newColumnMap = databaseService.getColumnsNameAndJdbcType(info);
		List<String> changedList = new LinkedList<>();
		Iterator<Entry<String, Integer>> newIterator = newColumnMap.entrySet().iterator();
		while(newIterator.hasNext()){
			Entry<String, Integer> entry = newIterator.next();
			String key = entry.getKey();
			Integer value = entry.getValue();
			if(!sourceColumnMap.containsKey(entry.getKey()) || value != sourceColumnMap.get(key)){
				changedList.add(key);
			}
		}
		
		return changedList.toArray(new String[changedList.size()]);
	}
	
	/**
	 * 更新数据源
	 * 
	 * @param id
	 * @param info
	 */
	public boolean updateDataSource(String id,DBCommonInfo info,Map<String,Map<String,Integer>> unsupportTable){
		logger.info("DS_DB : ["+id+"] start update-datasource task");
		validate(info);
		JAssert.isTrue(unsupportTable != null, "方法参数不能为空");
		JAssert.isTrue(!StringUtils.isEmpty(info.getTableName()), "表名不能为空");
		//根据数据源ID,验证数据源是否存在
		getDataSourceById(id,true);
		String dbConfigJson = ObjectMapperUtils.writeValue(info);
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		Map<String,Integer> columnMap = databaseService.getColumnsNameAndJdbcType(info);
		
		Map<String, Integer> unsupportColumnMap = getUnsupportColumns(columnMap);
		if(unsupportColumnMap.size() > 0){
			unsupportTable.put(info.getTableName(),unsupportColumnMap);
			logger.error("DS_DB : ["+id+"] has unsupport table column "+unsupportColumnMap.keySet() );
			return false;
		}
		
		String columnJson = ObjectMapperUtils.writeValue(columnMap);
		//更新数据源信息
		DataSource dataSource = new DataSource();
		dataSource.setId(id);
		dataSource.setLastModified(new Date());
		dataSource.setDataConfig(dbConfigJson);
		dataSource.setDataHead(columnJson);
		dataSource.setDataState(0);
		dataSource.setDataSize(0L);
		dataSource.setDataMessage("");
//		int updateNumber = update(dataSource);//todo
//		JAssert.isTrue(updateNumber == 1, "调用数据源更新失败 ID="+id);//todo
		logger.info("DS_DB : ["+id+"] execute update datasource success!");
		//执行数据迁移任务
		DataSourceTableMigrationService.Attachment attachment = new DataSourceTableMigrationService.Attachment();
//		attachment.setDbQuery(info);//todo
//		attachment.setUserContext(getUserContext());//todo
		attachment.setDatabaseService(databaseService);
		migrationService.migrate(id,attachment);
		return true;
	}
	
	
	/**
	 * 根据数据源ID，重新执行数据迁移任务
	 * 
	 * @param id
	 */
	public void createDataSourceAgain(String id){
		DataSource ds = getDataSourceById(id,true);
		String configJson = ds.getDataConfig();
		JAssert.isTrue(!StringUtils.isEmpty(configJson), "数据源连接配置读取错误");
		DBCommonInfo info = ObjectMapperUtils.readValue(configJson, DBCommonInfo.class);
		
		DataSource dataSource = new DataSource();
		dataSource.setId(id);
		dataSource.setLastModified(new Date());
		dataSource.setDataState(0);
		dataSource.setDataSize(0L);
		dataSource.setDataMessage("");
		//todo
//		int updateNumber = update(dataSource);
//		JAssert.isTrue(updateNumber == 1, "调用数据源更新失败 ID="+id);
//		Attachment attachment = new Attachment();
//		attachment.setDbQuery(info);
//		attachment.setUserContext(getUserContext());
//		attachment.setDatabaseService(getDatabaseService(info));
		
//		migrationService.migrate(id,attachment);
		//todo
	}
	
	private String createDefaultDataSourceName(String name,List<String> exsitNameList){
		String[] temps = name.split("_");
		String lastIndex = temps[temps.length - 1];
		String newName;
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$"); 
		if(pattern.matcher(lastIndex).matches()){
			int index = Integer.valueOf(lastIndex);
			index++;
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<temps.length-1;i++){
				sb.append(temps[i]);
				sb.append("_");
			}
			sb.append(index);
			newName = sb.toString();
		}else{
			newName = name + "_1";
		}
		
		if(exsitNameList.contains(newName)){
			return createDefaultDataSourceName(newName,exsitNameList);
		}
		return newName;
	}
	
	/**
	 * 创建数据源名称，如果在app_datasource表中的数据源名称已存在，则需要生成新的数据源名称
	 * 名称规则，如果datasourcename存在 则生产的名称为 datasourcename_1,
	 * 如果 datasourcename_1 存在，则生成的数据源名称为 datasourcename_2
	 * 
	 * @param info
	 * @return
	 */
	public List<DBDataSourceInfo> createManyDataSourceName(DBCommonInfo info){
		validate(info);
		List<DBDataSourceInfo> dataSources = info.getDataSources();
		JAssert.isTrue(dataSources != null, "提交参数不完整");
//		String userId = getUserContext().getOmSysUser().getUserId();//todo
		//从app_datasource表中读取所有的数据源名称
		List<String> nameList =null; //todo baseDao.listAllDataSourceName(userId);
		for(DBDataSourceInfo dsInfo : dataSources){
			List<String> tempList = new LinkedList<>();
			if(!StringUtils.isBlank(info.getDatabaseName())){
				tempList.add(info.getDatabaseName());
			}
			if(!StringUtils.isBlank(info.getSchemaName())){
				tempList.add(info.getSchemaName());
			}
			tempList.add(dsInfo.getTableName());
			String dataSourceName = StringUtils.join(tempList.toArray(new String[tempList.size()]),"_");
			if(nameList != null && nameList.size() > 0){
				for(String name : nameList){
					if(name.equals(dataSourceName)){
						dataSourceName = createDefaultDataSourceName(dataSourceName,nameList);
						break;
					}
				}
			}
			dsInfo.setDatasourceName(dataSourceName);
		}
		return dataSources;
	}
	
	
	/**
	 * 取消一个数据源正在的执行的数据迁移任务
	 * 
	 * @param id
	 */
	public void cancelDataSourceProcess(String id){
		logger.info("DS_DB : ["+id+"] cancel datasource task");
		//检查数据源在app_datasource表中是否存在
		getDataSourceById(id,true);
		//检查在执行任务中是否存在
		if(migrationService.exsits(id)){
			logger.info("DS_DB : ["+id+"] 执行任务删除");
			migrationService.shutdown(id);
		}else{
			logger.info("DS_DB : ["+id+"] 没有发现这个执行任务");
		}
		
		//int deleteNumber = dataSourceService.delete(id);
		//JAssert.isTrue(deleteNumber == 1, "调用数据源删除失败 ID="+id);
		//logger.info("DS_DB : ["+id+"] 成功删除该数据源");
	}
	
	
	/**
	 * 给定一个数据源ID，返回一个数据源实体
	 * 
	 * @param id
	 * @return
	 */
	public DataSource getDataSourceById(String id,boolean ifCheckType){
		DataSource ds = null;//todo this.get(id);
		JAssert.isTrue(ds != null, "数据源不存在！ID=" + id);
		if(ifCheckType){
			Integer dataType = ds.getDataType();
			JAssert.isTrue(dataType != null && dataType == 1, "非数据库类型数据源");
		}
		
		return ds;
	}
	
	/**
	 * 给定一个数据源ID，查询该数据库的连接信息
	 * 
	 * @param id
	 * @return
	 */
	public DBCommonInfo findDataSourceConfig(String id){
		DataSource ds = getDataSourceById(id,true);
		return ObjectMapperUtils.readValue(ds.getDataConfig(), DBCommonInfo.class);
	}
	
	/**
	 * 查询连接的数据库中的所有表名
	 * 
	 * @param info
	 * @return
	 */
	public String[] listTableName(DBCommonInfo info){
		validate(info);
		DataSourceDatabaseService databaseService = getDatabaseService(info);
		//listTable方法不会为null
		return databaseService.listTable(info);
	}
	
//	/**
//	 * 获取SESSION中的用户ID
//	 *
//	 * @return
//	 */
//	public UserContext getUserContext(){
//		UserContext userContext = UserContextHolder.getUserContext();
//		JAssert.isTrue(userContext != null, "用户数据为空");
//		OmSysUser user = userContext.getOmSysUser();
//		JAssert.isTrue(user != null, "用户数据为空");
//		JAssert.isTrue(!StringUtils.isEmpty(user.getId()), "用户ID为空");
//		return userContext;
//	}

}
