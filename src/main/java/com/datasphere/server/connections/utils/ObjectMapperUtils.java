package com.datasphere.server.connections.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ObjectMapperUtils {

	public static ObjectMapper objMapper = new ObjectMapper();
	
	public static <T> T readValue(String json, Class<T> clz) {
		try {
			return objMapper.readValue(json, clz);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static String writeValue(Object o) {
		try {
			return objMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public static <T> List<T> fromListJson(String str,Class<T> clazz){
		return new Gson().fromJson(str, new TypeToken<List<T>>(){}.getType());
	}
}
