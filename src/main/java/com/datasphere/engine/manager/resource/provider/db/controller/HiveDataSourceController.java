package com.datasphere.engine.manager.resource.provider.db.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;
import com.datasphere.engine.manager.resource.provider.service.UDSMService;

import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * hive
 */
@Controller
public class HiveDataSourceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HiveDataSourceController.class);

    public static final String BASE_PATH = "/datasource/hive";

    @Autowired
    UDSMService uDSMService;

    /**
     * test connection  hive
     * @param hiveConnectionInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH + "/testHive", method = RequestMethod.POST) 
    public Single<Map<String,Object>> testHive(@Body HiveConnectionInfo hiveConnectionInfo) {
        return Single.fromCallable(() -> {
            int result = uDSMService.testHive(hiveConnectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
        });
    }

    /**
     * select hive list table
     * @param hiveConnectionInfo
     * @hiveConnectionInfo
     */
	@RequestMapping(value = BASE_PATH + "/HiveListTable", method = RequestMethod.POST) 
    public Object HiveListTable(@Body HiveConnectionInfo hiveConnectionInfo){
        return Single.fromCallable(() -> {
            List<DBTableInfodmp> dbTableInfodmps = uDSMService.HiveListTable(hiveConnectionInfo);
            if(dbTableInfodmps == null){
                return JsonWrapper.failureWrapper("获取失败");
            }
            return JsonWrapper.successWrapper(dbTableInfodmps);
        });
    }

    /**
     * select hive table data 
     * @param hiveConnectionInfo
     * @hiveConnectionInfo
     */
	@RequestMapping(value = BASE_PATH + "/queryHiveTableData", method = RequestMethod.POST) 
    public Object queryHiveTableData(@Body HiveConnectionInfo hiveConnectionInfo){
        return Single.fromCallable(() -> {
            return JsonWrapper.successWrapper(uDSMService.queryHiveTableData(hiveConnectionInfo));
        });
    }

    /**
     * create hive datasource big data
     * @param hiveDataSourceInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH + "/createHive", method = RequestMethod.POST) 
    public Single<Map<String,Object>> createHive(@Body HiveDataSourceInfo hiveDataSourceInfo, HttpRequest request){
        return Single.fromCallable(() -> {
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int result = uDSMService.createHive(hiveDataSourceInfo,token);
            if (result == 0){
                return JsonWrapper.failureWrapper("插入失败");
            }
            return JsonWrapper.successWrapper("插入成功");
        });
    }

    /**
     * update hive datasource info
     * @param hiveDataSourceInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH + "/updateHive", method = RequestMethod.POST) 
    public Single<Map<String,Object>> updateHiveById(@Body HiveDataSourceInfo hiveDataSourceInfo, HttpRequest request){
        return Single.fromCallable(() -> {
            if(StringUtils.isBlank(hiveDataSourceInfo.getId())){
                return JsonWrapper.failureWrapper("id不能为空！");
            }
            //TODO 原有数据库是否被引用 -- 是否 可更新
            //TODO 查询数据库中有无数据源  验证名称是否重复
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int rsult = uDSMService.updateHiveById(hiveDataSourceInfo,token);
            if(rsult == 0){
                return JsonWrapper.failureWrapper("更新失败！");
            }
            return JsonWrapper.successWrapper();
        });
    }
}
