package com.datasphere.engine.core.utils;

import java.util.*;

public class JsonWrapper
{
    public static int DEFAULT_SUCCESS_CODE;
    public static int DEFAULT_FAILURE_CODE;
    
    static {
        JsonWrapper.DEFAULT_SUCCESS_CODE = 0;
        JsonWrapper.DEFAULT_FAILURE_CODE = 1;
    }
    
    public static Map<String, Object> successWrapper(final Object data) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", JsonWrapper.DEFAULT_SUCCESS_CODE);
        map.put("data", data);
        map.put("message", "Success");
        return map;
    }
    
    public static Map<String, Object> successWrapper() {
        return successWrapper(null);
    }
    
    public static Map<String, Object> failureWrapper(final int stateNo, final Object message) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", stateNo);
        map.put("data", null);
        map.put("message", message);
        return map;
    }
    
    public static Map<String, Object> failureWrapper(final Object message) {
        return failureWrapper(JsonWrapper.DEFAULT_FAILURE_CODE, message);
    }


//***********************作业返回信息****************************
    public static Map<String, Object> jobSuccess(final Object data, final String message) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("code", JsonWrapper.DEFAULT_SUCCESS_CODE);
        map.put("data", data);
        map.put("message", message);
        return map;
    }

    public static Map<String, Object> jobFailure(final int code, final String message) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("data", null);
        map.put("message", message);
        return map;
    }
}
