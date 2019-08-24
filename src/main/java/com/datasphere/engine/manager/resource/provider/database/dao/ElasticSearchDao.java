package com.datasphere.engine.manager.resource.provider.database.dao;

import com.datasphere.common.utils.RandomUtils;
import com.datasphere.engine.shaker.processor.buscommon.DatasetVersion;
import com.datasphere.server.manager.common.constant.ConnectionInfoAndOthers;
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


    public Boolean insertDatas(ConnectionInfoAndOthers ciao) throws Exception {
        String url = "http://"+ciao.getHostIP()+":"+
                ciao.getHostPort()+"/"+ciao.getDatabaseName()+"/"+ciao.getTableName()+"/";

        OkHttpClient httpClient = new OkHttpClient();
        Gson gson = new Gson();

//        try {
            Flowable
                .just("start")
                .flatMap(start -> {
                    return Flowable.<JsonObject>create(subscriber -> {
                        JsonObject objs = new Gson().fromJson(ciao.getDatas(), JsonObject.class);
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
        ConnectionInfoAndOthers ciao = new ConnectionInfoAndOthers();
        ciao.setBatchSize(50+"");
        ciao.setDatabaseName("es_test");
        ciao.setHostIP("117.107.241.79");
        ciao.setHostPort("9200");
        ciao.setUserName("");
        ciao.setUserPassword("");

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

        ciao.setDatas(datas);
        ciao.setTableName("es_test");
//		insertDatas(ciao);
    }


    public static void main1(String[] args) {
        String version = DatasetVersion.newVersion().getVersion();

        System.out.println("000"+RandomUtils.getNumStr_13());
        System.out.println(Long.parseLong(version, 16));
    }
}
