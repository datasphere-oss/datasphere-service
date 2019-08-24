package com.datasphere.engine.shaker.processor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.resource.manager.module.common.service.DaasUserTokenService;
import com.datasphere.common.utils.OkHttpRequest;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.shaker.processor.buscommon.ReturnMessageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 工作流-请求daas服务
 */
@Singleton
public class ProcessDaasService extends BaseService {
    private final static Logger logger = LoggerFactory.getLogger(ProcessDaasService.class);

    @Inject private DaasUserTokenService daasUserTokenService;

    public String runSqlOnDaas(String upper_sql) {
        //调用daas接口执行sql语句
        String job_id = executeSqlOnDaas(upper_sql);
        if (job_id.equals("")) return ReturnMessageUtils.DaasAuthorizationInvalid;
        job_id = JSON.parseObject(job_id).getString("id");
        String state = getJobState(job_id);
        System.out.println("The job [" +job_id+ "] is " + state);
 /*{ "schema": [{ "name": "id","type": { "name": "INTEGER" } }, { "name": "name","type": { "name": "VARCHAR" } } ],
	 "rowCount": 2,
	 "rows": [{ "name": "lisi", "id": 2}, { "name": "wangwu", "id": 3 } ] }*/
        String result = getJobResults(job_id);
        JSONObject jsonObject = JSON.parseObject(result);
        jsonObject.put("jobId", job_id);
        jsonObject.put("sql", upper_sql);
        jsonObject.put("message", "成功查询组件实例结果集");
        return jsonObject.toJSONString();
    }

    public String getJobState(String job_id) {
        String jobState = "";
        String result = "";
        while (!jobState.equals("COMPLETED")) {
            System.out.println("The job ["+job_id+"] is "+ jobState);
            String urlPath = this.daasServerAPIV3RootUrl + "/job/" + job_id;
            try {
                result = OkHttpRequest.okHttpClientGet(urlPath, daasUserTokenService.getCurrentToken());
                Thread.sleep(200);
            } catch (Exception e) {
                logger.error("ProcessService.getJobState(job_id):请求DAAS异常");
            } finally {
                jobState = JSON.parseObject(result).getString("jobState");
                if (jobState.equals("FAILED")) break;
            }
        }
        return jobState;
    }

    public String getJobResults(String job_id) {
        String results = "";
        String urlPath = this.daasServerAPIV3RootUrl + "/job/" +job_id+ "/results?offset=0&limit=100";
        System.out.println(urlPath);
        try {
            results = OkHttpRequest.okHttpClientGet(urlPath, daasUserTokenService.getCurrentToken());
        } catch (Exception e) {
            logger.error("ProcessService.getJobResults(job_id):请求DAAS异常");
        }
        return results;
    }

    public String executeSqlOnDaas(String sql) {
//	http://117.107.241.79:7090/apiv2/datasets/new_untitled_sql_and_run?newVersion=0002424622392793
        String urlPath = this.daasServerAPIV3RootUrl + "/sql";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("sql", sql.toString());
        try {
            return OkHttpRequest.okHttpClientPost(urlPath, jsonParam.toString(), daasUserTokenService.getCurrentToken());
        } catch (Exception e) {
            logger.error("ProcessService.executeSqlOnDaas(sql):请求DAAS异常");
        }
        return "";
    }

    public String getCatalogEntity(String dass_ds_id) {
        try {
            Thread.sleep(100);
            String catalogEntity = OkHttpRequest.okHttpClientGet(this.daasServerAPIV3RootUrl+"/catalog/"+dass_ds_id, daasUserTokenService.getCurrentToken());
// 正确：{"entityType":"source","config":{"hostname":"172.16.11.36","port":"3306","password":"$DREMIO_EXISTING_VALUE$","authenticationType":"MASTER","fetchSize":10},"state":{"status":"good","messages":[]},"id":"1c664927-ec0a-4561-b54e-79b6cbd07446","tag":"0","type":"MYSQL","name":"mysqlDaas牛逼测试2","description":"es高级测试详细信息","createdAt":"2018-09-25T10:19:33.269Z","metadataPolicy":{"authTTLMs":86400000,"namesRefreshMs":3600000,"datasetRefreshAfterMs":3600000,"datasetExpireAfterMs":10800000,"datasetUpdateMode":"PREFETCH_QUERIED"},"accelerationGracePeriodMs":10800000,"accelerationRefreshPeriodMs":3600000,"accelerationNeverExpire":false,"accelerationNeverRefresh":false,"children":[{"id":"a458f659-b3b9-4e4a-9a38-2059bc673b27","path":["mysqlDaas牛逼测试2","buffer_test"],"tag":"0","type":"CONTAINER","containerType":"FOLDER"},{"id":"9d534b66-2512-400c-8eff-4b095d009e4c","path":["mysqlDaas牛逼测试2","catalog_db"],"tag":"0","type":"CONTAINER","containerType":"FOLDER"},{"id":"b9a174b1-60f2-4213-a7eb-ef1ae36044d2","path":["mysqlDaas牛逼测试2","mysql"],"tag":"0","type":"CONTAINER","containerType":"FOLDER"},{"id":"993bdfd8-844c-455e-90aa-ee67a452b7dc","path":["mysqlDaas牛逼测试2","performance_schema"],"tag":"0","type":"CONTAINER","containerType":"FOLDER"},{"id":"aabb411e-b7c0-4066-98de-28ad27845acf","path":["mysqlDaas牛逼测试2","test"],"tag":"0","type":"CONTAINER","containerType":"FOLDER"}]}
// 错误：{"errorMessage":"The source [\"auto_843da4cc-db59-4c2d-98af-b08994d30873\"] is currently unavailable. Info: [[Message{level=ERROR, msg=Could not retrieve connection info from pool}]].","context":["ERROR Could not retrieve connection info from pool"],"moreInfo":""}
            System.err.println(catalogEntity);
            if(catalogEntity.contains("\"errorMessage\"")) return null;
            return JSON.parseObject(catalogEntity).getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
