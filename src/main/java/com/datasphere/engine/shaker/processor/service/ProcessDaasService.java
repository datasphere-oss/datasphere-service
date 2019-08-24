package com.datasphere.engine.shaker.processor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.resource.manager.module.common.service.DaasUserTokenService;
import com.datasphere.common.utils.OkHttpRequest;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.shaker.processor.buscommon.ReturnMessageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProcessDaasService extends BaseService {
    private final static Logger logger = LoggerFactory.getLogger(ProcessDaasService.class);

    @Autowired
    private DaasUserTokenService daasUserTokenService;

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
            System.err.println(catalogEntity);
            if(catalogEntity.contains("\"errorMessage\"")) return null;
            return JSON.parseObject(catalogEntity).getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
