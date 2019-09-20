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

public abstract class ReturnConst {
    public static Integer Success = 0;
    public static Integer Failed = 1;
    public static Integer Error = 9;
    public static Integer ParameterInvalid = 1001;
    public static Integer DataNotExist = 2001;
    public static Integer NotFountError = 404;
    public static Integer ServerError = 500;
    private static final Map<Integer, String> CODE = new HashMap<Integer, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(Success, "调用成功");
            put(Failed, "调用失败");
            put(Error, "系统内部错误");
            put(ParameterInvalid, "参数验证失败");
            put(DataNotExist, "数据不存在");
            put(NotFountError, "404錯誤");
            put(ServerError, "500錯誤");
        }
    };

    public static String get(Integer code) {
        return contains(code) ? CODE.get(code) : "未定义错误";
    }

    public static boolean contains(Integer code) {
        return CODE.containsKey(code);
    }
}