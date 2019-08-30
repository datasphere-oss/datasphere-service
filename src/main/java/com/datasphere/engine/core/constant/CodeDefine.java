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
