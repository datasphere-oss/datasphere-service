package com.datasphere.server.sso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.datasphere.common.utils.RandomUtils;
import com.datasphere.core.common.BaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class DSSVersionService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(DSSUserTokenService.class);

    private static  final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS).readTimeout(300, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).build();

    @Autowired
    DSSUserTokenService daasUserTokenService;

    private static String currentVersion;

    public String getCurrentVersion(JSONObject query) {
        //version
        currentVersion = getVersion(query);
        log.info(currentVersion);

        return currentVersion;
    }

    private String getVersion(JSONObject query) {
        //获取sql
        String version = "000" + RandomUtils.getNumStr_13();
        try {
            String secondPath = "/datasets/new_untitled?parentDataset=%22" +
                    query.get("spaceName") + "%22." +
                    query.get("dbName") + "." +
                    query.get("tableName") +
                    "&newVersion=" + version +
                    "&limit=150";// 单表
            String urlPath = this.daasServerAPIV2RootUrl + secondPath;
            String jsonStr = "{\"parentDataset\":\"'" + query.get("spaceName") +
                    "'." + query.get("dbName") + ".'" +
                    query.get("tableName")
                    + "'\",\"newVersion\":\"" +
                    version +
                    "\",\"limit\":\"1\"}";
            System.out.println(jsonStr);
            System.out.println(urlPath);
            RequestBody body = RequestBody.create(JSON, jsonStr);
            try {
                Request request = new Request.Builder()
                        .addHeader("Authorization", daasUserTokenService.getCurrentToken())
                        .header("Content-Type", "application/json")
                        .url(urlPath)
                        .post(body)
                        .build();
                Response response = httpClient.newCall(request).execute();
                if (response.code() != 200) {
                    log.error("DaasVersionService.getCurrentVersion(query):请求DAAS异常");
                    return null;
                }else if(response.code() == 200){
                    System.out.println(response.body().toString());
                    List<Map<String, String>> propertyValue = new ArrayList<>();
                    JsonParser jsonParser = new JsonParser();
                    JsonElement element = jsonParser.parse(response.body().toString());
                    JsonObject jsonObj = element.getAsJsonObject();
                    JsonObject contents = jsonObj.getAsJsonObject("contents");
                    JsonArray DBS = contents.getAsJsonArray("folders");
                    for (JsonElement jsonElement : DBS) {
                        System.out.println(jsonElement);
                        String DBName = jsonElement.getAsJsonObject().get("name").toString();
                        String DBName2 = DBName.substring(1, DBName.length() - 1);
                    }
                } else {
                    System.out.println(response.code());
                }
            }catch (Exception e){
            }
            return version;
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return null;
    }
}
