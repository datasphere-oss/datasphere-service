package com.datasphere.engine.manager.resource.provider.service;

import com.alibaba.fastjson.JSON;
import com.datasphere.common.data.Column;
import com.datasphere.common.data.Dataset;
import com.datasphere.common.utils.PageUtil;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.common.exception.http.HttpClientException;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.manager.resource.provider.catalog.model.RequestParams;
import com.datasphere.engine.manager.resource.provider.db.dao.DataSourceDao;
import com.datasphere.engine.manager.resource.provider.db.model.DBCommonInfo;
import com.datasphere.engine.manager.resource.provider.db.service.DataSourceDatabaseFactory;
import com.datasphere.engine.manager.resource.provider.db.service.DataSourceDatabaseService;
import com.datasphere.engine.manager.resource.provider.db.util.BeanToMapUtil;
import com.datasphere.engine.manager.resource.provider.elastic.model.DremioDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.hbase.model.HbaseConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hbase.model.HbaseDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.hbase.model.HbaseTableInfo;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.model.*;
import com.datasphere.engine.manager.resource.provider.webhdfs.model.WebHDFSConnectionInfo;
import com.datasphere.engine.manager.resource.provider.webhdfs.model.WebHDFSDataSourceInfo;
import com.datasphere.engine.shaker.processor.definition.ComponentDefinition;
import com.datasphere.engine.shaker.processor.definition.dao.ComponentDefinitionDao;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceDao;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.prep.service.ProgramService;
import com.datasphere.server.connections.dao.DataSetInstanceDao;
import com.datasphere.server.connections.model.DataSetInstance;
import com.datasphere.server.connections.service.DataAccessor;
import com.datasphere.server.sso.service.DSSUserTokenService;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * United Data Sources Management
 */
@Service
public class UDSMService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(UDSMService.class);
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static  final MediaType ESJSON  = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTO = "auto_";
    @Autowired
    ComponentInstanceService ciService;
    @Autowired
    ComponentInstanceRelationService cirService;
    @Autowired
    private ProgramService programService;
    @Autowired
    DSSUserTokenService dSSUserTokenService;
    @Autowired
    ExchangeSSOService exchangeSSOService;

    /**
     * 获得全部数据源信息
     * @return
     */
    public Map<String, Object> listAll(Integer pageIndex, Integer pageSize, String name , String token) {
        try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            String account = exchangeSSOService.getAccount(token);
            ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("pageIndex", PageUtil.getPageStart(pageIndex, pageSize));
            map.put("pageSize", pageSize);
            map.put("dataFrom", "DataWave");
            if("root".equals(account)){
                map.put("account",null);
                map.put("userId",null);
                map.put("departmentId",null);
            }else{
                map.put("account",account);
                map.put("userId",exchangeSSOService.getUserId(token));
                map.put("departmentId",exchangeSSOService.getCurDepAndSubDepIds(exchangeSSOService.getUserId(token),token));
            }
            Integer count = dataSourceDao.count(map);
            Map<String, Object> map1 = new HashMap<>();
            List<DataSource> sourceList = dataSourceDao.listAll(map);
            String conn;
            String str;
            for (int i = 0; i <sourceList.size() ; i++) {
                DataSource dataSource = sourceList.get(i);
                dataSource.setDataQuote(componentInstanceDao.getCountByCFId(sourceList.get(i).getId()));
                //TODO 后期清库之后换成getConnection即可
                Map maps = (Map) JSON.parse(dataSource.getDataConfig());
                if("HBASE".equals(dataSource.getDataDSType())){
                    dataSource.setConnectionURL(String.valueOf(maps.get("HBaseConfigurationPath")));
                    continue;
                }else{
                    if(maps.containsKey("connectionURL")){
                        if("ORACLE".equals(dataSource.getDataDSType())){
                            String connectionURL = String.valueOf(maps.get("connectionURL"));
                            conn = connectionURL.substring(connectionURL.indexOf("@") + 1);
                            str = conn.substring(0, conn.indexOf(":"));
                        }else{
                            String connectionURL = String.valueOf(maps.get("connectionURL"));
                            conn = connectionURL.substring(connectionURL.indexOf("/") + 2);
                            str = conn.substring(0, conn.indexOf("/"));
                        }
                    }else{
                        str = String.valueOf(maps.get("hostIP"))+":"+String.valueOf(maps.get("hostPort"));
                    }
                }
                dataSource.setConnectionURL(str);
                //TODO
            }
            map1.put("list", dataSourceDao.listAll(map));
            map1.put("totalRecords", count);
            return map1;
        }
    }

    /**
     * 查询表信息，包括表名、表总记录数、表字段个数
     *
     * @param info
     * @return
     */
    public List<DBTableInfodmp> listTableInfo(DBCommonInfo info) {
        try{
            // validate(info);
            DataSourceDatabaseService databaseService = getDatabaseService(info);
            List<DBTableInfodmp> infoList = databaseService.listTableInfo(info);
            //infoList 不会为null
            JAssert.isTrue(infoList.size() > 0, "没有发现数据库");
            return infoList;
        }catch (Exception e){
            log.info("{}",e);
            return null;
        }
    }

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
            case "PostgreSQL" :
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
     * 创建多个数据源
     * @param dataSourceInfo
     * @return
     */
    public int create(DBDataSourceInfo dataSourceInfo,String token) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            //数据源
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            //组建定义
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
//            DataSetInstanceDao dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);

            List<DBTableInfo> dataSources = dataSourceInfo.getDataSources();
            int result = 0;
            if (dataSources != null && dataSources.size() > 0) {
                for (int i = 0; i < dataSources.size(); i++) {
                    DataSource dataSource = new DataSource();
                    String id = UUID.randomUUID().toString();
                    dataSource.setId(id);
                    dataSource.setName(dataSources.get(i).getName());
                    dataSource.setDataDesc(dataSources.get(i).getDataDesc());
                    dataSource.setBusinessType(dataSourceInfo.getBusinessType());
                    dataSource.setDataDSType(dataSourceInfo.getDataDSType());
                    dataSource.setDataType(dataSourceInfo.getDataType());
                    dataSource.setDataFrom("DataWave");
                    dataSource.setClassification("001");
                    dataSource.setCode("SimpleDataSource");
                    dataSource.setCreateTime(new Date());
                    dataSource.setCreator(exchangeSSOService.getAccount(token));
                    //数据连接信息
                    DBConnectionInfo connectionInfo = new DBConnectionInfo();
                    connectionInfo.setTypeName(dataSourceInfo.getDataDSType());
                    connectionInfo.setDatabaseName(dataSourceInfo.getDatabaseName());
                    connectionInfo.setHostIP(dataSourceInfo.getHostIP());
                    connectionInfo.setHostPort(dataSourceInfo.getHostPort());
                    connectionInfo.setUserName(dataSourceInfo.getUserName());
                    connectionInfo.setUserPassword(dataSourceInfo.getUserPassword());
                    connectionInfo.setTableName(dataSources.get(i).getTableName());
                    connectionInfo.setColumns(dataSources.get(i).getColumns());
                    connectionInfo.setRows(dataSources.get(i).getRows());
                    String dataConfig = new Gson().toJson(connectionInfo);
                    dataSource.setDataConfig(dataConfig);
                    //insert dataset_instance
//                    Map<String,Object> data = queryTableData(connectionInfo);
//                    DataSetInstance dataSetInstance = new DataSetInstance();
//                    dataSetInstance.setId(id);
//                    dataSetInstance.setColumnsJSON(new Gson().toJson(data.get("meta")));
//                    dataSetInstance.setMessage(dataSources.get(i).getTableName());
//                    dataSetInstanceDao.insert(dataSetInstance);

                    //新增组件定义
                    ComponentDefinition cd = new ComponentDefinition();
                    cd.setId(id);
                    cd.setCode(ComponentClassification.SimpleDataSource.name());
                    cd.setName(dataSources.get(i).getName());
                    cd.setClassification("001");
                    cd.setCreator(exchangeSSOService.getAccount(token));
                    cd.setParams("");
                    dictionaryDao.insert(cd);
                    //新增数据源
                    dataSourceDao.insert(dataSource);
                    result++;
                }
                sqlSession.commit();
            }
            return result;
//        }catch (SQLException e){
//            log.error("{}",e);
//        }
//        return 0;
        }
    }

    /**
     * 测试database
     * @param connectionInfo
     * @return
     */
    public int testDatabase(DBConnectionInfo connectionInfo) {
        ResultInfo resultInfo = null;
        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {

            resultInfo = httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/database/test-connection",connectionInfo),
                            ResultInfo.class);
            if(resultInfo != null && resultInfo.isData()){
                return 1;
            }
        } catch(Exception ex) {
            log.error(ex.toString());
        }
        return 0;
    }

    /**
     * 查询数据集
     * @param connectionInfo
     * @return
     */
    public Map<String,Object> queryTableData(DBConnectionInfo connectionInfo) {

        Map<String,Object> lst = null;

        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {
            //为空 默认select *
            //TODO 分页未实现
            if(StringUtils.isBlank(connectionInfo.getQuery())){
                if("MYSQL".equals(connectionInfo.getTypeName())){
                    connectionInfo.setQuery("select * from " + connectionInfo.getTableName()+ " limit 0,100");
                } else if("ORACLE".equals(connectionInfo.getTypeName())) {
                    connectionInfo.setQuery("select * from \"" + connectionInfo.getTableName()+ "\" where rownum <= 100");
                } else if("POSTGRES".equals(connectionInfo.getTypeName())){
                    connectionInfo.setQuery("select * from " + connectionInfo.getTableName()+ " limit 100 offset 0;");
                }
            }

            String result= httpClient.toBlocking().retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/database/query",connectionInfo));
            //处理数据
            if(result == null) {
                return lst;
            }
            Type listType = new TypeToken<Map<String,Object>>(){}.getType();
            lst = new Gson().fromJson(result, listType);
            return lst;
        } catch(Exception ex) {
            log.error(ex.toString());
        }
        return lst;
    }

    /**
     * 更新数据元名称及描述
     * @param dataSource
     * @return
     */
    public int update(DataSource dataSource) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);

            int result = dataSourceDao.update(dataSource);

            ComponentDefinition cd = new ComponentDefinition();
            cd.setId(dataSource.getId());
            cd.setName(dataSource.getName());
            dictionaryDao.update(cd);//更新数据源
            sqlSession.commit();
            return result;
        }
    }

    /**
     * 根据ID查询数据源信息
     * @param id
     * @return
     */
    public DataSource findDataSourceById(String id) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            DataSource dataSource = dataSourceDao.findDataSourceById(id);
            return dataSource;
        }
    }


    /**
     * 删除数据源
     * @param ids
     * @return
     */
    public int deleteDatasourceById(String ids) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            List<String> idlst = Splitter.on("^^").splitToList(ids == null ? "" : ids);

            int num = 0;
            for (int i = 0; i < idlst.size() ; i++) {
                //TODO 查询被应用的次数  为0 可以删除
                num += dataSourceDao.deleteDatasourceById(idlst.get(i));
            }
            sqlSession.commit();
            return num;
        }
    }

    /**
     * 更新数据源
     * @param dataSourceInfo
     * @return
     */
    public int updateDatasourceById(DBDataSourceInfo dataSourceInfo,String token) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);

            List<DBTableInfo> dataSources = dataSourceInfo.getDataSources();
            int result = 0;
            if(dataSources != null && dataSources.size() == 1){
                DataSource dataSource = new DataSource();
                dataSource.setId(dataSourceInfo.getId());
                dataSource.setName(dataSources.get(0).getName());
                dataSource.setDataDesc(dataSources.get(0).getDataDesc());
                dataSource.setBusinessType(dataSourceInfo.getBusinessType());
                dataSource.setDataDSType(dataSourceInfo.getDataDSType());
                dataSource.setDataType(dataSourceInfo.getDataType());
                dataSource.setDataFrom("DataWave");
                dataSource.setLastModified(new Date());
                dataSource.setCreator(exchangeSSOService.getAccount(token));
                //数据连接信息
                DBConnectionInfo connectionInfo = new DBConnectionInfo();
                connectionInfo.setTypeName(dataSourceInfo.getDataDSType());
                connectionInfo.setTableName(dataSources.get(0).getTableName());
                connectionInfo.setDatabaseName(dataSourceInfo.getDatabaseName());
                connectionInfo.setHostIP(dataSourceInfo.getHostIP());
                connectionInfo.setHostPort(dataSourceInfo.getHostPort());
                connectionInfo.setUserName(dataSourceInfo.getUserName());
                connectionInfo.setUserPassword(dataSourceInfo.getUserPassword());
                connectionInfo.setColumns(dataSources.get(0).getColumns());
                connectionInfo.setRows(dataSources.get(0).getRows());
                String dataConfig = new Gson().toJson(connectionInfo);
                dataSource.setDataConfig(dataConfig);

                ComponentDefinition cd = new ComponentDefinition();
                cd.setId(dataSource.getId());
                cd.setName(dataSource.getName());
                cd.setLastModified(new Date());
                dictionaryDao.update(cd);//更新数据源

                result = dataSourceDao.updateDatasourceById(dataSource);
                sqlSession.commit();
            }
            return result;
        }
    }

    /**
     *  校验name
     * @param name
     * @return
     */
    public List<String> verifyDatasourceName(String name) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            List<String> namelst = Splitter.on("^^").splitToList(name);
            List<String> names = new ArrayList<>();
            for (int i = 0; i < namelst.size(); i++) {
                //TODO 根据名称和用户查询是否有数据
                int count = dataSourceDao.findDatasourceByName(namelst.get(i));
                if (count > 0){
                    names.add(namelst.get(i));
                }
            }
            return names;
        }
    }

    /**
     * 查询已定阅资源
     * @return
     */
    public Map<String,Object> getSubscribeDatasource(RequestParams requestParams) {
        List<DataSource> dataSourceList = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        try(HttpClient httpClient = HttpClient.create(new URL(this.OpenAPIServerRootUrl));
            SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()
        ) {
            DataSourceDao baseDao = sqlSession.getMapper(DataSourceDao.class);
//            if (requestParams.getStatus() == 0){
            String result = httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.OpenAPIServerRootUrl + "/datasource/getSubscribeDatasource",
                                    requestParams));
            Map<String, List<Map<String,String>>>  gsonMap = new Gson()
                    .fromJson(result, new TypeToken<Map<String, List<Map<String,String>>>>() {
                    }.getType());
            List<Map<String, String>> data = gsonMap.get("data");
            for (int i = 0; i <data.size() ; i++) {
                DataSource dataSource = new DataSource();
                dataSource.setId(data.get(i).get("id"));
                dataSource.setName(data.get(i).get("name"));
                dataSource.setDataConfig(data.get(i).get("dataConfig"));
                if("relation_database".equals(data.get(i).get("dataType"))){
                    dataSource.setDataType(0);
                }else if("file_storage".equals(data.get(i).get("dataType"))){
                    dataSource.setDataType(1);
                }else if("big_data".equals(data.get(i).get("dataType"))){
                    dataSource.setDataType(2);
                }else if("analysis_calculation_engine".equals(data.get(i).get("dataType"))){
                    dataSource.setDataType(3);
                }else if("Interface_catalog".equals(data.get(i).get("dataType"))){
                    dataSource.setDataType(4);
                }
                dataSource.setDataDSType(data.get(i).get("dataDSType"));
                dataSource.setDataDesc(data.get(i).get("dataDesc"));
                dataSource.setBusinessType(data.get(i).get("businessType"));
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataSource.setCreateTime(date.parse(data.get(i).get("subscribeTime")));
                dataSource.setDataSize(0L);
                dataSource.setCreator(data.get(i).get("creator"));
                dataSource.setDataFrom("已定阅");
                dataSourceList.add(dataSource);

                DataSource dataSourceById = baseDao.findDataSourceById(dataSource.getId());
                if(dataSourceById == null){
                    baseDao.insert(dataSource);
                }else{
                    baseDao.updateDatasourceById(dataSource);
                }
            }
            sqlSession.commit();

//            }
            Map<String,Object> map = new HashMap<>();
            map.put("name",requestParams.getName());
            map.put("pageIndex",PageUtil.getPageStart(
                    requestParams.getPageIndex(),requestParams.getPageSize()));
            map.put("pageSize",requestParams.getPageSize());
            map.put("dataFrom","已定阅");
            List<DataSource> dataSourceList1 = baseDao.listAll(map);
            map1.put("list",dataSourceList1);
            map1.put("totalRecords",baseDao.count(map));

//            requestParams.setStatus(1);
            return map1;
        } catch(Exception ex) {
            log.error("{}",ex.toString());
        }
        return map1;
    }



//*****************************************组件***********************************************
    /**
     * 通过组件实例id获取数据源详细信息和列表
     * @param id
     * @return
     */
    public DataSource findDataSourceDetail(String id,String token) {
        ComponentInstance cinstances = getInstance(id);//根据id获取 组件实例
        String creator = exchangeSSOService.getAccount(token);
        JAssert.isTrue(cinstances != null, "组件实例不存在：" + id);
        DataSource dataSource = null;
        if (cinstances != null) {
//				String dsId = instances.get("componentDefinitionId").toString();
            String cdId = cinstances.getComponentDefinitionId();//从 组件实例 中获取 组件定义ID
            try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
                DataSourceDao baseDao = sqlSession.getMapper(DataSourceDao.class);
                dataSource = baseDao.findDataSourceById(cdId);//根据ID查询datasource
            }
            if (dataSource != null && GlobalDefine.COMPONENT_CLASSIFICATION.MY_DATASOURCE.equals(dataSource.getClassification())) {
                if (!creator.equals(dataSource.getCreator())) dataSource = null;// 我的数据源，较验创建者
            }
            JAssert.isTrue(dataSource != null, "数据源不存在：id:" + cdId + ",用户:" + creator);
            if (dataSource != null) {
                try {
                    DataAccessor dataAccessor = new DataAccessor(DataAccessor.PG_URL);
					Dataset dataset = dataAccessor.getDatasetMetadata(cdId); //datasetDAO.get(key);
                    JAssert.isTrue(dataset != null, "dataset_instance表不存在！keyId=" + cdId);
                    List<Map<String, String>> columns = new ArrayList<Map<String, String>>();
                    Column[] columns2 = dataset.getColumnsMeta();
                    if (columns2 != null && columns2.length > 0) {
                        for (int i = 0; i < columns2.length; i++) {
                            Map<String, String> colMap = new LinkedHashMap<>();
                            colMap.put(GlobalDefine.DATASOURCE_COLUMNNAME, columns2[i].getName());
                            colMap.put(GlobalDefine.DATASOURCE_COLUMNTYPE, columns2[i].getType());
                            columns.add(colMap);
                        }
                        dataSource.setDataColumnsType(columns);
//                        dataSource.setDataPerform(String.valueOf(dataAccessor.rowCount(cdId)));
//                        dataSource.setDataPerform(JSON.parseObject(dataSource.getDataConfig().toString()).getString("rows"));
//                        dataSource.setDataColumns(String.valueOf(columns2.length));
                        Gson gson = new Gson();
                        DBConnectionInfo dbConnectionInfo = gson.fromJson(dataSource.getDataConfig(),DBConnectionInfo.class);

                        dataSource.setDataFileType(dbConnectionInfo.getTypeName());
                        dataSource.setDataPerform(dbConnectionInfo.getRows());
                        dataSource.setDataColumns(dbConnectionInfo.getColumns());
                        dataSource.setDataFileName(dataSource.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dataSource;
    }

    /**
     * 根据组件实例id，获取组件实例
     * @param id
     * @return
     */
    public ComponentInstance getInstance(String id) {

        return ciService.get(id);
    }

    /**
     * 通过组件实例id获取组件实例预处理信息
     * @param processId
     * @return
     */
    public Map<String, Object> dataPreProcess(String processId) {
        Map<String, Object> preProcess = new HashMap<>();
        // TODO 组建处理相关
//		PreProcess= (Map)WebsiteTerminalUtils.sendRequest(preprocessTerminal,HttpMethod.GET, "/program/getDefault?processId="+id,"");
//        ProgramOutputData data = programService.getDefaultProgram(processId);
        ComponentInstance data = ciService.get(processId);

        //查询getoutput（上一级组件字段信息）
        List<Object> getoutput = new ArrayList<>();
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()){
            DataSetInstanceDao dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);
            List<ComponentInstanceRelation> cirList= cirService.getComponentInstanceRelationsByDestinationId(processId);
            if(cirList != null && cirList.size() != 0){
                for (ComponentInstanceRelation list :cirList) {
                    DataSetInstance dataSetInstance = dataSetInstanceDao.get(list.getSourceComponentInstanceId());
                    getoutput.add(dataSetInstance);
                }
            }else{
                getoutput = null;
            }
        }

        preProcess.put("DataPreProcess",data);
        preProcess.put("getOutPut",getoutput);
        return preProcess;
    }

//**************************************hive**************************************
    /**
     * test hive connection
     * @param hiveConnectionInfo
     * @return
     */
    public int testHive(HiveConnectionInfo hiveConnectionInfo) {
        ResultInfo resultInfo = null;
        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {

            resultInfo = httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/bigdata/hive/test-connection",hiveConnectionInfo),
                            ResultInfo.class);
            if(resultInfo != null && resultInfo.isData()){
                return 1;
            }
        } catch(Exception ex) {
            log.error(ex.toString());
        }
        return 0;
    }

    /**
     * create hive datasource
     * @param hiveDataSourceInfo
     * @return
     */
    public int createHive(HiveDataSourceInfo hiveDataSourceInfo,String token) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            //数据源
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            //组建定义
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
            Gson gson = new Gson();

            List<HiveTableInfo> dataSources = hiveDataSourceInfo.getDataSources();
            int result = 0;
            if(dataSources != null && dataSources.size() > 0){
                for (int i = 0; i < dataSources.size() ; i++) {
                    DataSource dataSource =
                            gson.fromJson(gson.toJson(hiveDataSourceInfo), DataSource.class);
                    String id = UUID.randomUUID().toString();
                    dataSource.setId(id);
                    dataSource.setName(dataSources.get(i).getName());
                    dataSource.setDataDesc(dataSources.get(i).getDataDesc());
                    dataSource.setDataFrom("DataWave");
                    dataSource.setCreateTime(new Date());
                    dataSource.setCreator(exchangeSSOService.getAccount(token));
                    //hive数据连接信息
                    HiveConnectionInfo connectionInfo =
                            gson.fromJson(gson.toJson(hiveDataSourceInfo), HiveConnectionInfo.class);
                    connectionInfo.setTypeName(hiveDataSourceInfo.getDataDSType());
                    connectionInfo.setTableName(dataSources.get(i).getTableName());
                    connectionInfo.setColumns(dataSources.get(i).getColumns());
                    connectionInfo.setRows(dataSources.get(i).getRows());
                    String dataConfig = gson.toJson(connectionInfo);
                    dataSource.setDataConfig(dataConfig);

                    //新增组件定义
                    ComponentDefinition cd = new ComponentDefinition();
                    cd.setId(id);
                    cd.setCode(ComponentClassification.SimpleDataSource.name());
                    cd.setName(dataSources.get(i).getName());
                    cd.setClassification("001");
                    cd.setCreator(exchangeSSOService.getAccount(token));
                    cd.setParams(dataConfig);
                    dictionaryDao.insert(cd);
                    //新增数据源
                    dataSourceDao.insert(dataSource);
                    result ++;
                }
                sqlSession.commit();
            }
            return result;
        }
    }

    /**
     * 查询hive表列表
     * @param hiveConnectionInfo
     * @return
     */
    public List<DBTableInfodmp> HiveListTable(HiveConnectionInfo hiveConnectionInfo) {
        List<DBTableInfodmp> lst = null;

        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {

            String result= httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/bigdata/hive/table-list",hiveConnectionInfo));
            //处理数据
            if(result == null){
                return lst;
            }
            Type listType = new TypeToken<Map<String,List<DBTableInfodmp>>>(){}.getType();
            Map<String,List<DBTableInfodmp>> map = new Gson().fromJson(result, listType);
            lst = map.get("data");
            return lst;
        } catch(Exception ex) {
            log.error("{}",ex);
            return null;
        }
    }

    /**
     * select hive table data
     * @param hiveConnectionInfo
     * @return
     */
    public Map<String,Object> queryHiveTableData(HiveConnectionInfo hiveConnectionInfo) {
        Map<String,Object> lst = null;

        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {
            //为空 默认select *
            //TODO 分页未实现
            if(StringUtils.isBlank(hiveConnectionInfo.getQuery())){
                hiveConnectionInfo.setQuery("select * from " + hiveConnectionInfo.getTableName() +" limit 100");
            }

            String result= httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/bigdata/hive/query",hiveConnectionInfo));
            //处理数据
            if(result == null){
                return lst;
            }
            Type listType = new TypeToken<Map<String,Object>>(){}.getType();
            lst = new Gson().fromJson(result, listType);
            return lst;
        } catch(Exception ex) {
            log.error(ex.toString());
        }
        return lst;
    }

    /**
     * update hive datasource info
     * @param hiveDataSourceInfo
     * @return
     */
    public int updateHiveById(HiveDataSourceInfo hiveDataSourceInfo,String token) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            //数据源
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            //组建定义
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
            Gson gson = new Gson();

            List<HiveTableInfo> dataSources = hiveDataSourceInfo.getDataSources();
            int result = 0;
            if(dataSources != null && dataSources.size() == 1){
                for (int i = 0; i < dataSources.size() ; i++) {
                    DataSource dataSource =
                            gson.fromJson(gson.toJson(hiveDataSourceInfo), DataSource.class);
                    dataSource.setName(dataSources.get(i).getName());
                    dataSource.setDataDesc(dataSources.get(i).getDataDesc());
//                    dataSource.setDataFrom("DataWave");
                    dataSource.setLastModified(new Date());
                    dataSource.setCreator(exchangeSSOService.getAccount(token));
                    //hive数据连接信息
                    HiveConnectionInfo connectionInfo =
                            gson.fromJson(gson.toJson(hiveDataSourceInfo), HiveConnectionInfo.class);
                    connectionInfo.setTypeName(hiveDataSourceInfo.getDataDSType());
                    connectionInfo.setTableName(dataSources.get(i).getTableName());
                    connectionInfo.setColumns(dataSources.get(i).getColumns());
                    connectionInfo.setRows(dataSources.get(i).getRows());
                    String dataConfig = gson.toJson(connectionInfo);
                    dataSource.setDataConfig(dataConfig);

                    //更新组件定义
                    ComponentDefinition cd = new ComponentDefinition();
                    cd.setId(hiveDataSourceInfo.getId());
                    cd.setCode(ComponentClassification.SimpleDataSource.name());
                    cd.setName(dataSources.get(i).getName());
                    cd.setCreator(exchangeSSOService.getAccount(token));
                    dictionaryDao.update(cd);
                    //更新数据源
                    dataSourceDao.updateDatasourceById(dataSource);
                    result ++;
                }
                sqlSession.commit();
            }
            return result;
        }
    }


    /**
     * 获取单个的数据源
     * @param id
     * @return
     */
    public DataSourceWithAll getWithPanel(String id,String token) {
        List<DataSourceWithAll> all = null;
        String userId = exchangeSSOService.getAccount(token);
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao baseDao = sqlSession.getMapper(DataSourceDao.class);
            all = baseDao.getWithAll(id, userId);
        }
        if (all != null && all.size() > 0) {
            DataSourceWithAll dataSourceWithAll = all.get(0);
            List panels = getCitedByPanel(dataSourceWithAll.getId(), userId);
            O.json(panels);
            if (panels != null && panels.size() > 0) {
                for (Object panel : panels) {
                    Map<String, String> panelMap = (Map<String, String>) panel;
                    Map<String, String> componentDefinitionPanel = new HashMap<>();
                    componentDefinitionPanel.put("panelId", panelMap.get("id"));
                    componentDefinitionPanel.put("panelName", panelMap.get("panelName"));
                    componentDefinitionPanel.put("projectId", panelMap.get("projectId"));
                    componentDefinitionPanel.put("componentDefinitionId", dataSourceWithAll.getId());
                    //jeq
//					JAssert.isTrue(projectService.get(panelMap.get("projectId")) != null, "该项目不存在：" + panelMap.get("projectId"));
//					componentDefinitionPanel.put("projectName", projectService.get(panelMap.get("projectId")).getProjectName());
                    dataSourceWithAll.addPanels(componentDefinitionPanel);
                }
                dataSourceWithAll.setPanelCount((long) panels.size());
            }
            return dataSourceWithAll;
        } else {
            return null;
        }
    }

    /**
     * 获取单个数据源被引用面板详细信息
     * @param id
     * @param userId
     * @return
     */
    public List getCitedByPanel(String id,String userId) {
        List  panels=null;
        try {
            String params="id="+id+"&creator="+userId;
            // TODO  面板相关
//			panels= (List)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/sourceTrace", params);
            O.json(panels);
        } catch (HttpClientException e) {
            e.printStackTrace();
            throw new JIllegalOperationException(e.getErrorCode(),e.getMessage());
        }
        return panels;
    }


//**************************************hbase**************************************
    /**
     * test connection
     * @param hbaseConnectionInfo
     * @return
     */
    public int testHBase(HbaseConnectionInfo hbaseConnectionInfo) {
        ResultInfo resultInfo = null;
        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {

            resultInfo = httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/bigdata/hbase/test-connection",hbaseConnectionInfo),
                            ResultInfo.class);
            if(resultInfo != null && resultInfo.isData()){
                return 1;
            }
        } catch(Exception ex) {
            log.error(ex.toString());
        }
        return 0;
    }

    /**
     * select hbase list table info
     * @param hbaseConnectionInfo
     * @return
     */
    public List<DBTableInfodmp> hBaseListTable(HbaseConnectionInfo hbaseConnectionInfo) {
        List<DBTableInfodmp> lst = null;

        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {

            String result= httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/bigdata/hbase/table-list",hbaseConnectionInfo));
            //处理数据
            if(result == null){
                return lst;
            }
            Type listType = new TypeToken<Map<String,List<DBTableInfodmp>>>(){}.getType();
            Map<String,List<DBTableInfodmp>> map = new Gson().fromJson(result, listType);
            lst = map.get("data");
            return lst;
        } catch(Exception ex) {
            log.error("{}",ex);
            return null;
        }
    }

    /**
     * query hbase table data
     * @param hbaseConnectionInfo
     * @return
     */
    public Map<String,Object> queryHBaseTableData(HbaseConnectionInfo hbaseConnectionInfo) {
        Map<String,Object> lst = null;

        try(HttpClient httpClient = HttpClient.create(new URL(this.dataServiceOnPrestoServerRootUrl))) {

            String result= httpClient
                    .toBlocking()
                    .retrieve(
                            POST(this.dataServiceOnPrestoServerRootUrl + "/bigdata/hbase/query",hbaseConnectionInfo));
            //处理数据
            if(result == null){
                return lst;
            }
            Type listType = new TypeToken<Map<String,Object>>(){}.getType();
            lst = new Gson().fromJson(result, listType);
            return lst;
        } catch(Exception ex) {
            log.error(ex.toString());
        }
        return lst;
    }

    /**
     * create hbase datasource
     * @param hbaseDataSourceInfo
     * @return
     */
    public int createHBase(HbaseDataSourceInfo hbaseDataSourceInfo,String token) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            //数据源
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            //组建定义
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
            Gson gson = new Gson();

            List<HbaseTableInfo> dataSources = hbaseDataSourceInfo.getDataSources();
            int result = 0;
            if(dataSources != null && dataSources.size() > 0){
                for (int i = 0; i < dataSources.size() ; i++) {
                    DataSource dataSource =
                            gson.fromJson(gson.toJson(hbaseDataSourceInfo), DataSource.class);
                    String id = UUID.randomUUID().toString();
                    dataSource.setId(id);
                    dataSource.setName(dataSources.get(i).getName());
                    dataSource.setDataDesc(dataSources.get(i).getDataDesc());
                    dataSource.setDataFrom("DataWave");
                    dataSource.setClassification("001");
                    dataSource.setCreateTime(new Date());
                    dataSource.setCreator(exchangeSSOService.getAccount(token));
                    //hbase数据连接信息
                    HbaseConnectionInfo connectionInfo =
                            gson.fromJson(gson.toJson(hbaseDataSourceInfo), HbaseConnectionInfo.class);
                    connectionInfo.setTypeName(hbaseDataSourceInfo.getDataDSType());
                    connectionInfo.setTableName(dataSources.get(i).getTableName());
                    connectionInfo.setColumns(dataSources.get(i).getColumns());
                    connectionInfo.setRows(dataSources.get(i).getRows());
                    String dataConfig = gson.toJson(connectionInfo);
                    dataSource.setDataConfig(dataConfig);

                    //新增组件定义
                    ComponentDefinition cd = new ComponentDefinition();
                    cd.setId(id);
                    cd.setCode(ComponentClassification.SimpleDataSource.name());
                    cd.setName(dataSources.get(i).getName());
                    cd.setClassification("001");
                    cd.setCreator(exchangeSSOService.getAccount(token));
                    cd.setParams("");
                    dictionaryDao.insert(cd);
                    //新增数据源
                    dataSourceDao.insert(dataSource);
                    result ++;
                }
                sqlSession.commit();
            }
            return result;
        }
    }

    /**
     * update hbase datasource by id
     * @param hbaseDataSourceInfo
     * @return
     */
    public int updateHBaseById(HbaseDataSourceInfo hbaseDataSourceInfo,String token) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            //数据源
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            //组建定义
            ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
            Gson gson = new Gson();

            List<HbaseTableInfo> dataSources = hbaseDataSourceInfo.getDataSources();
            int result = 0;
            if(dataSources != null && dataSources.size() == 1){
                for (int i = 0; i < dataSources.size() ; i++) {
                    DataSource dataSource =
                            gson.fromJson(gson.toJson(hbaseDataSourceInfo), DataSource.class);
                    dataSource.setName(dataSources.get(i).getName());
                    dataSource.setDataDesc(dataSources.get(i).getDataDesc());
//                    dataSource.setDataFrom("DataWave");
                    dataSource.setLastModified(new Date());
                    dataSource.setCreator(exchangeSSOService.getAccount(token));
                    //hbase数据连接信息
                    HbaseConnectionInfo connectionInfo =
                            gson.fromJson(gson.toJson(hbaseDataSourceInfo), HbaseConnectionInfo.class);
                    connectionInfo.setTypeName(hbaseDataSourceInfo.getDataDSType());
                    connectionInfo.setTableName(dataSources.get(i).getTableName());
                    connectionInfo.setColumns(dataSources.get(i).getColumns());
                    connectionInfo.setRows(dataSources.get(i).getRows());
                    String dataConfig = gson.toJson(connectionInfo);
                    dataSource.setDataConfig(dataConfig);

                    //更新组件定义
                    ComponentDefinition cd = new ComponentDefinition();
                    cd.setId(hbaseDataSourceInfo.getId());
                    cd.setCode(ComponentClassification.SimpleDataSource.name());
                    cd.setName(dataSources.get(i).getName());
                    cd.setCreator(exchangeSSOService.getAccount(token));
                    dictionaryDao.update(cd);
                    //更新数据源
                    dataSourceDao.updateDatasourceById(dataSource);
                    result ++;
                }
                sqlSession.commit();
            }
            return result;
        }
    }

    public String createSource(DremioDataSourceInfo es) {
        String json = null;
        es.setName(AUTO + UUID.randomUUID().toString());
        es.setDescription(AUTO + es.getType());
        Gson gson = new Gson();
        json = gson.toJson(es);
        RequestBody body = RequestBody.create(ESJSON, json);
        {
            {
                log.info("create dataSource");
                try {
                    String url = this.daasServerAPIV3RootUrl+"/source";
                    Request request = new Request.Builder()
                            .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
                            .url(url)
                            .post(body)
                            .build();
                    Response response = httpClient.newCall(request).execute();
                    log.info(response.toString());
                    String responseBody = response.body().string();
                    if (response.code() != 200) return "失败";
                    log.info("{}", responseBody);
                    System.out.println(response.code());
                    return responseBody;
                } catch (Exception ex) {
                    log.error("ex: {}", ex);
                }
            }
        }
        return null;
    }


//    public String createSource(ElasticsearchInfo es) {
//
//        System.out.println(es.getName());
//            String json= "{\n" +
//                                 "\t\"name\": \""+es.getName()+"\",\n" +
//                                 "\t\"description\": \"es高级测试详细信息\",\n" +
//                                 "\t\"type\": \"ELASTIC\",\n" +
//                                 "\t\"config\": {\n" +
//                                 "\t\t\"username\": \"\",\n" +
//                                 "\t\t\"password\": \"\",\n" +
//                                 "\t\t\"hostList\": [{\n" +
//                                 "\t\t\t\t\"hostname\": \"172.16.11.36\",\n" +
//                                 "\t\t\t\t\"port\": \"9200\"\n" +
//                                 "\t\t\t}\n" +
//                                 "\t\t],\n" +
//                                 "\t\t\"authenticationType\":\"MASTER\",\n" +
//                                 "\t\t\"scriptsEnabled\": false,\n" +
//                                 "\t\t\"showHiddenIndices\": false,\n" +
//                                 "\t\t\"sslEnabled\": false,\n" +
//                                 "\t\t\"showIdColumn\": false,\n" +
//                                 "\t\t\"readTimeoutMillis\":200,\n" +
//                                 "\t\t\"scrollTimeoutMillis\": 200,\n" +
//                                 "\t\t\"usePainless\": false,\n" +
//                                 "\t\t\"useWhitelist\":false,\n" +
//                                 "\t\t\"scrollSize\":200\n" +
//                                 "\t},\n" +
//                                 "\t\"metadataPolicy\": {\n" +
//                                 "\t\t\"authTTLMs\": 60000,\n" +
//                                 "\t\t\"datasetRefreshAfterMs\": 60000,\n" +
//                                 "\t\t\"datasetExpireAfterMs\": 60000,\n" +
//                                 "\t\t\"namesRefreshMs\": 60000,\n" +
//                                 "\t\t\"datasetUpdateMode\":\"PREFETCH_QUERIED\"\n" +
//                                 "\t},\n" +
//                                 "\t\"accelerationRefreshPeriodMs\": 200,\n" +
//                                 "\t\"accelerationGracePeriodMs\": 200\n" +
//                                 "}";
//
//            RequestBody body = RequestBody.create(ESJSON, json);
//            {
//                {
//                    log.info("list all sources");
//                    try {
//                        String url = ROOT_URL + "/api/v3/source";
//                        String token = "_dremiobl5e2pvh00c6i3csm1n3mfqp8i";
//                        Request request = new Request.Builder()
//                                                  .addHeader("Authorization", token)
//                                                  .url(url)
//                                                  .post(body)
//                                                  .build();
//
//                        Response response = httpClient.newCall(request).execute();
//
//                        String responseBody=response.body().string();
//                        log.info("{}", responseBody);
//                        return responseBody;
//                    } catch (Exception ex) {
//                        log.error("ex: {}", ex);
//                    }
//                }
//            }
//            return null;
//
//    }

//**************************************webHDFS**************************************

    /**
     * test WEBHDFS connection
     * @param webHDFSConnectionInfo
     * @return
     */
    public int testWebHDFS(WebHDFSConnectionInfo webHDFSConnectionInfo) {
        return 0;
    }

    public List<DBTableInfodmp> webHDFSListFiles(WebHDFSConnectionInfo webHDFSConnectionInfo) {
        return null;
    }

    public int updateWebHDFS(WebHDFSDataSourceInfo webHDFSDataSourceInfo) {
        return 0;
    }

    public int createWebHDFS(WebHDFSDataSourceInfo webHDFSDataSourceInfo) {
        return 0;
    }

//**********DAAS查询：ES、hive、hbase、json、csv*****************************************
    @Autowired
    DataQueryService dataQueryService;

    public Map<String, Object> queryDaas(DBQuery dbQuery) {
        String cd_id = dbQuery.getId();
        String db_name = dbQuery.getDatabaseName();
        String table_name = dbQuery.getTableName();
        String SQL = dbQuery.getQuery();
        String full_name;
        try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
            String daas_id = dataSourceDao.getDaasId(cd_id);
            String daas_name = dataSourceDao.getDaasName(daas_id);
            full_name = "\""+daas_name+"\""+"."+db_name+"."+table_name;
            SQL = SQL.replace(SQL.split(" ")[3], full_name);
        }
        Map<String, Object> rs = dataQueryService.dataQuery(SQL+" limit 1000");
        Map<String, Object> rs2 = new HashMap<>();
        rs2.put("data", rs.get("rows"));
        rs2.put("meta", rs.get("schema"));
        rs2.put("rows", rs.get("rowCount"));
        rs2.put("tableName", full_name);
        return rs2;
    }

    /**
     * 数据查询：(on-presto + daas)
     * @param dbQuery
     * @return
     */
    public Map<String, Object> queryTableDataById(DBQuery dbQuery) {
        Map<String, Object> result = new HashMap<>();
        if(GlobalDefine.COMPONENT_CLASSIFICATION.MY_DATASOURCE.equals(dbQuery.getClassification())) {
            DataSource dataSource = findDataSourceById(dbQuery.getId());
            if(dataSource != null) {
                String dataConfig = dataSource.getDataConfig();

                Type listType = new TypeToken<Map<String,Object>>(){}.getType();
                Map<String,Object> dataConfig2 = new Gson().fromJson(dataConfig, listType);

                DBConnectionInfo dbConnectionInfo = new DBConnectionInfo();
                String type = dataSource.getDataDSType();
                dbConnectionInfo.setTypeName(type);
                if ("ELASTIC".equals(type)) {
                    List<Map<String, String>> hostlist = new Gson()
                            .fromJson(dataConfig2.get("hostList").toString(), new TypeToken<List<Map<String, String>>>() {}.getType());
                    dbConnectionInfo.setHostIP(hostlist.get(0).get("hostname").toString());
                    dbConnectionInfo.setHostPort(hostlist.get(0).get("port").toString());
                } else {
                    dbConnectionInfo.setHostIP(dataConfig2.get("hostname").toString());
                    dbConnectionInfo.setHostPort(dataConfig2.get("port").toString());
                }
                dbConnectionInfo.setUserName(dataConfig2.get("username").toString());
                dbConnectionInfo.setUserPassword(dataConfig2.get("password").toString());
                String tableName = "";
                // OnPresto-RDS:mysql,oracle,pg
                if ("MYSQL".equals(type) || "POSTGRES".equals(type) || "ORACLE".equals(type)) {
                    if ("ORACLE".equals(type)) {//
                        dbConnectionInfo.setDatabaseName(dataConfig2.get("instance").toString());
                        tableName = '"'+dataConfig2.get("tableName").toString()+'"';
                        dbConnectionInfo.setTableName(dataConfig2.get("tableName").toString());
                    } else {
                        dbConnectionInfo.setDatabaseName(dataConfig2.get("databaseName").toString());
                        if ("MYSQL".equals(type)) {
                            tableName = dataConfig2.get("tableName").toString();
                            dbConnectionInfo.setTableName(tableName);
                        } else if ("POSTGRES".equals(type)) {
                            tableName = dataConfig2.get("scheme") +"."+dataConfig2.get("tableName");
                            dbConnectionInfo.setTableName(dataConfig2.get("scheme") +"."+dataConfig2.get("tableName"));
                        }
                    }
                    //测试连接信息是否可用
                    if(testDatabase(dbConnectionInfo) == 0) {
                        result.put("exception", "数据库连接信息有变动，查询失败请更新！");
                    } else {
                        String sql = dbQuery.getQuery();
                        String spaceName = sql.split(" ")[3];
                        sql = sql.replace(spaceName, tableName);
                        dbConnectionInfo.setQuery(sql);
                        result = queryTableData(dbConnectionInfo);
                        if(result == null) result.put("exception", "数据库信息查询失败，请检查表是否存在！");
                    }
                } else { //其他数据源去daas查询：es,hive,hbase,json,csv
                    dbQuery.setDatabaseName(dataConfig2.get("databaseName").toString());
                    dbQuery.setTableName(dataConfig2.get("tableName").toString());
                    result = queryDaas(dbQuery);
                    if(result == null) result.put("exception", "数据库信息查询失败，请检查表是否存在！");
                }
            } else {
                result.put("exception", "数据源不存在！");
            }
        } else {
            result.put("exception", "本次查询的不是数据源！");
        }
        return result;
    }
}
