package com.datasphere.engine.manager.resource.provider.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.hbase.model.HbaseConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hbase.model.HbaseDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;
import com.datasphere.engine.manager.resource.provider.service.DataSourceService;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * hbase
 */
//@Validated
public class HbaseDatasourceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HbaseDatasourceController.class);

    public static final String BASE_PATH = "/datasource/hbase";

    @Inject
    DataSourceService dataSourceService;

    /**
     * test connection  hbase    - on-presto
     * @param hbaseConnectionInfo
     * @return
     */
    @Post(BASE_PATH + "/testHBase")
    public Single<Map<String,Object>> testHBase(@Body HbaseConnectionInfo hbaseConnectionInfo) {
        return Single.fromCallable(() -> {
            int result = dataSourceService.testHBase(hbaseConnectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
        });
    }

    /**
     * select hbase list table   - on-presto
     * @param hbaseConnectionInfo
     * @hbaseConnectionInfo
     */
    @Post(BASE_PATH + "/hBaseListTable")
    public Object hBaseListTable(@Body HbaseConnectionInfo hbaseConnectionInfo){
        return Single.fromCallable(() -> {
            List<DBTableInfodmp> dbTableInfodmps = dataSourceService.hBaseListTable(hbaseConnectionInfo);
            if(dbTableInfodmps == null){
                return JsonWrapper.failureWrapper("获取失败");
            }
            return JsonWrapper.successWrapper(dbTableInfodmps);
        });
    }

    /**
     * select hbase table data    - on-presto
     * @param hbaseConnectionInfo
     * @hbaseConnectionInfo
     */
    @Post(BASE_PATH + "/queryHBaseTableData")
    public Object queryHBaseTableData(@Body HbaseConnectionInfo hbaseConnectionInfo){
        return Single.fromCallable(() -> {
            return JsonWrapper.successWrapper(dataSourceService.queryHBaseTableData(hbaseConnectionInfo));
        });
    }

    /**
     * create hbase datasource big data
     * @param hbaseDataSourceInfo
     * @return
     */
    @Post(BASE_PATH + "/createHBase")
    public Single<Map<String,Object>> createHBase(@Body HbaseDataSourceInfo hbaseDataSourceInfo, HttpRequest request){
        return Single.fromCallable(() -> {
            if(hbaseDataSourceInfo.getBusinessType() == null){
                return JsonWrapper.failureWrapper("业务类型不能为空");
            }
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int result = dataSourceService.createHBase(hbaseDataSourceInfo,token);
            if (result == 0){
                return JsonWrapper.failureWrapper("插入失败");
            }
            return JsonWrapper.successWrapper("插入成功");
        });
    }

    /**
     * update hbase datasource info
     * @param hbaseDataSourceInfo
     * @return
     */
    @Post(BASE_PATH + "/updateHBase")
    public Single<Map<String,Object>> updateHBaseById(@Body HbaseDataSourceInfo hbaseDataSourceInfo, HttpRequest request){
        return Single.fromCallable(() -> {
            if(StringUtils.isBlank(hbaseDataSourceInfo.getId())){
                return JsonWrapper.failureWrapper("id不能为空！");
            }
            //TODO 原有数据库是否被引用 -- 是否 可更新
            //TODO 查询数据库中有无数据源  验证名称是否重复
            String token = request.getParameters().get("token");
            if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int rsult = dataSourceService.updateHBaseById(hbaseDataSourceInfo,token);
            if(rsult == 0){
                return JsonWrapper.failureWrapper("更新失败！");
            }
            return JsonWrapper.successWrapper();
        });
    }
}
