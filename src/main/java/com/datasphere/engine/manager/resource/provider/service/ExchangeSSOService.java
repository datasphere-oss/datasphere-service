package com.datasphere.engine.manager.resource.provider.service;

import com.datasphere.common.utils.OkHttpRequest;
import com.datasphere.core.common.BaseService;
import com.datasphere.server.manager.module.login.service.LoginServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public class ExchangeSSOService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(ExchangeSSOService.class);
	@Inject
    LoginServiceImpl loginService;

	private static final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();

	/**
	 * 根据token获取userId
	 * @param token
	 * @return
	 */
	public String getUserId(String token){
		try{
			String userInfoUrl = loginService.getUserLoginInfoByTokenApi().replace("{token}", token);
			Request request = new Request.Builder()
									  .url(userInfoUrl)
									  .get()
									  .build();
			Response response = httpClient.newCall(request).execute();
			String responseBody = response.body().string();
			Gson gson = new Gson();
//			JSONArray jsonArray = null;
			Map<String,Object> config = gson.fromJson(responseBody,
					new TypeToken<Map<String,Object>>(){}.getType());
//			jsonArray = new JSONArray(responseBody);
			String userId = (String) config.get("userId");
			System.out.println("用户ID:"+userId);
			return userId;
		} catch (Exception e) {
		}
		return null ;
	}


	/**
	 * 根据token获取Account
	 * @param token
	 * @return
	 */
	public String getAccount(String token){
		try{
			String userInfoUrl = loginService.getUserLoginInfoByTokenApi().replace("{token}", token);
			Request request = new Request.Builder()
									  .url(userInfoUrl)
									  .get()
									  .build();
			Response response = httpClient.newCall(request).execute();
			String responseBody = response.body().string();
			Gson gson = new Gson();
			Map<String,Object> config = gson.fromJson(responseBody, new TypeToken<Map<String,Object>>(){}.getType());
			String username = (String) config.get("account");
			System.out.println("用户名:"+username);
			return username;
		} catch (Exception e){
		}
		return null ;
	}

	/**
	 * 根据userId获取部门Id
	 * @param userId
	 * @return
	 */
	public String getCurDepIdByUserId(String userId,String token){
		try {
			String url = this.permissionServer + this.getUserDepartmentIds +"/" + userId+"?token="+token;
			String result = OkHttpRequest.okHttpClientGet(url);
			if(result == null){
				return null;
			}
			Map<String,String> map = new Gson().fromJson(result,
					new TypeToken<Map<String,String>>(){}.getType());
			return map.get("depId");
		}catch (Exception e){
			log.error("{}",e);
			return null;
		}
	}

	/**
	 * 根据userId获取部门名称
	 * @param userId
	 * @return
	 */
	public String getCurDepNameIdByUserId(String userId,String token){
		try {
			String url = this.permissionServer + this.getUserDepartmentIds +"/" + userId+"?token="+token;
			String result = OkHttpRequest.okHttpClientGet(url);
			if(result == null){
				return null;
			}
			Map<String,String> map = new Gson().fromJson(result,
					new TypeToken<Map<String,String>>(){}.getType());
			return map.get("depName");
		}catch (Exception e){
			log.error("{}",e);
			return null;
		}
	}

	/**
	 * 根据userId获取所有有部门id
	 * @param userId
	 * @return
	 */
	public List<String> getCurDepAndSubDepIds(String userId,String token){
		try {
			String url = this.permissionServer + this.getUserDepartmentIds +"/" + userId+"?token="+token;
			String result = OkHttpRequest.okHttpClientGet(url);
			if(result == null){
				return null;
			}
			List<String> list = new Gson().fromJson(result,
					new TypeToken<List<String>>(){}.getType());
			if(list == null || list.size() == 0) return null;
			return list;
		}catch (Exception e){
			log.error("{}",e);
			return null;
		}
	}
}
