package com.datasphere.engine.manager.resource.provider.db.dao;

import com.datasphere.common.utils.RandomUtils;
import com.datasphere.engine.datasource.connections.constant.ConnectionInfo;
import com.datasphere.engine.shaker.processor.buscommon.DatasetVersion;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ElasticSearchDao {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchDao.class);


    public Boolean insertDatas(ConnectionInfo ci) throws Exception {
        String url = "http://"+ci.getHostIP()+":"+
        		ci.getHostPort()+"/"+ci.getDatabaseName()+"/"+ci.getTableName()+"/";

        OkHttpClient httpClient = new OkHttpClient();
        Gson gson = new Gson();

//        try {
            Flowable
                .just("start")
                .flatMap(start -> {
                    return Flowable.<JsonObject>create(subscriber -> {
                        JsonObject objs = new Gson().fromJson(ci.getDatas(), JsonObject.class);
                        JsonArray rows = objs.getAsJsonArray("rows");
                        for (int k = 0; k < rows.size(); k++){
                            JsonObject item = rows.get(k).getAsJsonObject();
                            subscriber.onNext(item);
                        }

                        subscriber.onComplete();
                    }, BackpressureStrategy.BUFFER);
                })
                .subscribe(
                    (item) -> {
                        log.info("{}", gson.toJson(item));

                        RequestBody body = RequestBody.create(JSON, gson.toJson(item));
                        Request request = new Request.Builder()
                                .url(url + UUID.randomUUID().toString())
                                .post(body)
                                .build();

                        Response response = httpClient.newCall(request).execute();
                        log.info("{}", response.body().string());
                    }
                );

//        } catch (Exception ex) {
//
//        }
        return true;
    }


    public static void main(String[] args) {
        ConnectionInfo ci = new ConnectionInfo();
        ci.setBatchSize(50+"");
        ci.setDatabaseName("es_test");
        ci.setHostIP("127.0.0.1");
        ci.setHostPort("9200");
        ci.setUserName("");
        ci.setUserPassword("");

        String datas = "{\n" +
                "\t\t\t\"schema\": [\n" +
                "\t\t\t  { \"name\": \"id\",\"type\": { \"name\": \"INTEGER\" } },\n" +
                "\t\t\t  { \"name\": \"name\",\"type\": { \"name\": \"VARCHAR\" } },\n" +
                "\t\t\t  { \"name\": \"age\",\"type\": { \"name\": \"INTEGER\" } }\n" +
                "\t\t\t],\n" +
                "\t\t\t\"rowCount\": 2,\n" +
                "\t\t\t\"rows\": [\n" ;
				datas +="\t\t\t\t{ \"name\": \"jjj\", \"id\": 10,\"age\": 10},\n";
//        for(int i=0;i<10000;i++){
//            datas += "{ \"name\": \"lisi"+i+"\", \"id\": "+ i +"},\n";
//        }


        datas += "\t\t\t\t{ \"name\": \"wangwu\", \"id\": 9,\"age\": 9}\n" +
                "\t\t\t]\n" +
                "\t\t}";

        ci.setDatas(datas);
        ci.setTableName("es_test");
//		insertDatas(ciao);
    }


    public static void main1(String[] args) {
        String version = DatasetVersion.newVersion().getVersion();

        System.out.println("000"+RandomUtils.getNumStr_13());
        System.out.println(Long.parseLong(version, 16));
    }
}
