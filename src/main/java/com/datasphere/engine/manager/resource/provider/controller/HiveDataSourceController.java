package com.datasphere.engine.manager.resource.provider.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;
import com.datasphere.engine.manager.resource.provider.service.DataSourceService;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
//import io.micronaut.validation.Validated;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * hive
 */
//@Validated
public class HiveDataSourceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HiveDataSourceController.class);

    public static final String BASE_PATH = "/datasource/hive";

    @Inject
    DataSourceService dataSourceService;

    /**
     * test connection  hive    - on-presto
     * @param hiveConnectionInfo
     * @return
     */
    @Post(BASE_PATH + "/testHive")
    public Single<Map<String,Object>> testHive(@Body HiveConnectionInfo hiveConnectionInfo) {
        return Single.fromCallable(() -> {
            int result = dataSourceService.testHive(hiveConnectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
        });
    }

    /**
     * select hive list table   - on-presto
     * @param hiveConnectionInfo
     * @hiveConnectionInfo
     */
    @Post(BASE_PATH + "/HiveListTable")
    public Object HiveListTable(@Body HiveConnectionInfo hiveConnectionInfo){
        return Single.fromCallable(() -> {
            List<DBTableInfodmp> dbTableInfodmps = dataSourceService.HiveListTable(hiveConnectionInfo);
            if(dbTableInfodmps == null){
                return JsonWrapper.failureWrapper("获取失败");
            }
            return JsonWrapper.successWrapper(dbTableInfodmps);
        });
    }

    /**
     * select hive table data    - on-presto
     * @param hiveConnectionInfo
     * @hiveConnectionInfo
     */
    @Post(BASE_PATH + "/queryHiveTableData")
    public Object queryHiveTableData(@Body HiveConnectionInfo hiveConnectionInfo){
        return Single.fromCallable(() -> {
            return JsonWrapper.successWrapper(dataSourceService.queryHiveTableData(hiveConnectionInfo));
        });
    }

    /**
     * create hive datasource big data
     * @param hiveDataSourceInfo
     * @return
     */
    @Post(BASE_PATH + "/createHive")
    public Single<Map<String,Object>> createHive(@Body HiveDataSourceInfo hiveDataSourceInfo, HttpRequest request){
        return Single.fromCallable(() -> {
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int result = dataSourceService.createHive(hiveDataSourceInfo,token);
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
    @Post(BASE_PATH + "/updateHive")
    public Single<Map<String,Object>> updateHiveById(@Body HiveDataSourceInfo hiveDataSourceInfo, HttpRequest request){
        return Single.fromCallable(() -> {
            if(StringUtils.isBlank(hiveDataSourceInfo.getId())){
                return JsonWrapper.failureWrapper("id不能为空！");
            }
            //TODO 原有数据库是否被引用 -- 是否 可更新
            //TODO 查询数据库中有无数据源  验证名称是否重复
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int rsult = dataSourceService.updateHiveById(hiveDataSourceInfo,token);
            if(rsult == 0){
                return JsonWrapper.failureWrapper("更新失败！");
            }
            return JsonWrapper.successWrapper();
        });
    }
}
