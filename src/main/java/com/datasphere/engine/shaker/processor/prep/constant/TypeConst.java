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

package com.datasphere.engine.shaker.processor.prep.constant;

import java.util.HashMap;
import java.util.Map;

public abstract class TypeConst {

    public static final String STRING = "String";

    public static final String INTEGER = "Integer";

    public static final String DECIMAL = "Decimal";

    public static final String DATETIME = "Datetime";

    public static final String BOOLEAN = "Boolean";

    private static final Map<String, String> TYPE = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(STRING, "String");
            put(INTEGER, "Integer");
            put(DECIMAL, "Decimal");
            put(DATETIME, "Datetime");
            put(BOOLEAN, "Boolean");
        }
    };

    public static String get(String type) {
        return contains(type) ? TYPE.get(type) : "Type Undefined";
    }

    public static boolean contains(String type) {
        return TYPE.containsKey(type);
    }

}
