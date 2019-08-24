/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.common.utils;

import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class OkHttpRequest {
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).build();

    /**
     * okHttpclient：调用DSS的API
     * @param url：
     *           String secondPath = "/datasets/new_untitled?parentDataset=%22MySqL-1111%22.catalog_db.test&newVersion=0000808889220291&limit=150";
     *           String urlPath = this.daasServerAPIV2RootUrl + secondPath;
     * @param jsonStr：
     *           String jsonStr = "{\"parentDataset\":\"'MySqL-1111'.catalog_db.'test'\",\"newVersion\":\"0000808889220291\",\"limit\":\"150\"}";
     * @param authorization：this.daasServerAuthorization
     * @return
     * @throws Exception
     */
    public static String okHttpClientPost(String url, String jsonStr, String authorization) throws Exception {
//        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder().addHeader("Authorization", authorization)
                .url(url)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String okHttpClientGet(String url, String authorization) throws Exception {
//        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().addHeader("Authorization", authorization)
                .url(url)
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String okHttpClientGet(String url) throws Exception {
//        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
}
