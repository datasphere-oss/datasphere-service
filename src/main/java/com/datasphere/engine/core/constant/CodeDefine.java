package com.datasphere.engine.core.constant;

public class CodeDefine
{
    public static class JSON_ERROR_CODE
    {
        public static Integer REQ_JSON_PARSE;
        public static Integer JSON_TO_STRING;
        public static Integer COMPONENT_NAME_CONSISTENCY;
        
        static {
            JSON_ERROR_CODE.REQ_JSON_PARSE = 100;
            JSON_ERROR_CODE.JSON_TO_STRING = 101;
            JSON_ERROR_CODE.COMPONENT_NAME_CONSISTENCY = 102;
        }
    }
}
