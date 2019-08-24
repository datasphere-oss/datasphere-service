package com.datasphere.engine.shaker.processor.buscommon.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
	public static String post(String url, Map<String, String> data) throws Exception {
		String resp="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 拼接参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Entry<String, String> key : data.entrySet()) {
			nvps.add(new BasicNameValuePair(key.getKey(), key.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = httpclient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			resp = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		}
		response.close();
		return resp;
	}
	public static String get(String url)throws Exception {

		String resp="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet=new HttpGet(url);
		
		CloseableHttpResponse response = httpclient.execute(httpGet);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			resp = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		}
		response.close();
		return resp;
	}
}
