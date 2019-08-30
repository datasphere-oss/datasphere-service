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

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulate the data returned by the Controller
 */
public class ResponseBodyUtils {
    private static Map<String, Object> map = new HashMap<>();

    public static Map<String, Object> success(Object data, String description) {
        map.put("data", data);
        map.put("success", true);
        map.put("msg", description);
        return map;
    }

    public static Map<String, Object> failure(Object data, String description) {
        map.put("data", data);
        map.put("success", false);
        map.put("msg", description);
        return map;
    }
}
