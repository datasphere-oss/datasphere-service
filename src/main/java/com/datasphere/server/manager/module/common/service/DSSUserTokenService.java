package com.datasphere.server.manager.module.common.service;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.datasphere.core.common.BaseService;
import com.datasphere.engine.server.user.UserLogin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class DSSUserTokenService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(DSSUserTokenService.class);

    private static  final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");

    private static String currentToken;


    public String getCurrentToken() {
        if(currentToken == null){
            //login
            currentToken = login();
        }else{
            //验证 token
            currentToken = verifyToken();
        }
        log.info(currentToken);
        return currentToken;
    }


    private String login() {
        Gson gson = new Gson();
        OkHttpClient httpClient = new OkHttpClient();
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(this.daasUsername);
        userLogin.setPassword(this.daasPassword);
        RequestBody body = RequestBody.create(JSON, gson.toJson(userLogin));
        {
            {
                try {
                    String url = this.daasServerAPIV2RootUrl + "/login";
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    Response response = httpClient.newCall(request).execute();
                    String responseBody=response.body().string();
                    String code ;
                    if (response.code()!=200){
                        return null;
                    }
                    System.out.println(response.code());
                    Map<String, Object> gsonMap = new Gson()
                            .fromJson(responseBody, new TypeToken<Map<String, Object>>() {
                            }.getType());

                    return "_daas"+gsonMap.get("token").toString();
                } catch (Exception ex) {
                    log.error("ex: {}", ex);
                }
            }
        }
        return null;
    }

    private String verifyToken(){
        OkHttpClient httpClient = new OkHttpClient();
        try {
            String url = this.daasServerAPIV2RootUrl + "/spaces?nocache=1539163721265";
            Request request = new Request.Builder()
                    .addHeader("Authorization", currentToken)
                    .url(url)
                    .get()
                    .build();
            Response response = httpClient.newCall(request).execute();
            if (response.code()!=200){
                return login();
            }else{
                return currentToken;
            }
        } catch (Exception ex) {
            log.error("ex: {}", ex);
        }
        return null;
    }


}
