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
