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

package com.datasphere.engine.core.utils;

import com.datasphere.engine.common.exception.JRuntimeException;
import com.datasphere.engine.core.constant.CodeDefine;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.google.gson.Gson;

import java.util.*;

public class ObjectMapperUtils
{
    public static ObjectMapper objMapper;
    
    static {
        ObjectMapperUtils.objMapper = new ObjectMapper();
    }
    
    public static <T> T readValue(final String json, final Class<T> clz) {
        try {
            return (T)ObjectMapperUtils.objMapper.readValue(json, (Class)clz);
        }
        catch (Exception e) {
            throw new JRuntimeException(CodeDefine.JSON_ERROR_CODE.REQ_JSON_PARSE, e.getMessage());
        }
    }
    
    public static String writeValue(final Object o) {
        try {
            return ObjectMapperUtils.objMapper.writeValueAsString(o);
        }
        catch (JsonProcessingException e) {
            throw new JRuntimeException(CodeDefine.JSON_ERROR_CODE.JSON_TO_STRING, e.getMessage());
        }
    }
    
    public static <T> List<T> fromListJson(final String str, final Class<T> clazz) {
        return (List<T>) new Gson().fromJson(str, (Class)clazz);
    }
}
