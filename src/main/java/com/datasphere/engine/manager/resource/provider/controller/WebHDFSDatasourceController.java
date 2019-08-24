package com.datasphere.engine.manager.resource.provider.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;
import com.datasphere.engine.manager.resource.provider.service.DataSourceService;
import com.datasphere.engine.manager.resource.provider.webhdfs.model.WebHDFSConnectionInfo;
import com.datasphere.engine.manager.resource.provider.webhdfs.model.WebHDFSDataSourceInfo;

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
 * Created by jxm 2018/8/30
 */
//@Validated
public class WebHDFSDatasourceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(WebHDFSDatasourceController.class);

    public static final String BASE_PATH = "/datasource/webhdfs";

    @Inject
    DataSourceService dataSourceService;

    /**
     * test connection  webhdfs    - on-presto
     * @param webHDFSConnectionInfo
     * @return
     */
    @Post(BASE_PATH + "/testWebHDFS")
    public Single<Map<String,Object>> testWebHDFS(@Body WebHDFSConnectionInfo webHDFSConnectionInfo) {
        return Single.fromCallable(() -> {
            int result = dataSourceService.testWebHDFS(webHDFSConnectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
        });
    }

    /**
     * select webhdfs list table   - on-presto
     * @param webHDFSConnectionInfo
     * @webhdfsConnectionInfo
     */
    @Post(BASE_PATH + "/webHDFSListFiles")
    public Object webHDFSListFiles(@Body WebHDFSConnectionInfo webHDFSConnectionInfo){
        return Single.fromCallable(() -> {
            List<DBTableInfodmp> dbTableInfodmps = dataSourceService.webHDFSListFiles(webHDFSConnectionInfo);
            if(dbTableInfodmps == null){
                return JsonWrapper.failureWrapper("获取失败");
            }
            return JsonWrapper.successWrapper(dbTableInfodmps);
        });
    }

    /**
     * create webhdfs datasource big data
     * @param webHDFSDataSourceInfo
     * @return
     */
    @Post(BASE_PATH + "/createWebHDFS")
    public Single<Map<String,Object>> createWebHDFS(@Body WebHDFSDataSourceInfo webHDFSDataSourceInfo){
        return Single.fromCallable(() -> {
            if(webHDFSDataSourceInfo.getBusinessType() == null){
                return JsonWrapper.failureWrapper("业务类型不能为空");
            }
            int result = dataSourceService.createWebHDFS(webHDFSDataSourceInfo);
            if (result == 0){
                return JsonWrapper.failureWrapper("插入失败");
            }
            return JsonWrapper.successWrapper("插入成功");
        });
    }

    /**
     * update WebHDFS datasource info
     * @param webHDFSDataSourceInfo
     * @return
     */
    @Post(BASE_PATH + "/updateWebHDFS")
    public Single<Map<String,Object>> updateWebHDFS(@Body WebHDFSDataSourceInfo webHDFSDataSourceInfo){
        return Single.fromCallable(() -> {
            if(StringUtils.isBlank(webHDFSDataSourceInfo.getId())){
                return JsonWrapper.failureWrapper("id不能为空！");
            }
            //TODO 原有数据库是否被引用 -- 是否 可更新
            //TODO 查询数据库中有无数据源  验证名称是否重复

            int rsult = dataSourceService.updateWebHDFS(webHDFSDataSourceInfo);
            if(rsult == 0){
                return JsonWrapper.failureWrapper("更新失败！");
            }
            return JsonWrapper.successWrapper();
        });
    }

}
