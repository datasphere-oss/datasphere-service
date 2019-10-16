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

package com.datasphere.engine.shaker.processor.utils;

/*{
    "1": "大于",
    "2": "小于",
    "3": "等于",
    "4": "大于等于",
    "5": "小于等于",
    "6": "不等于",
    "7": "开头是",
    "8": "结尾是",
    "9": "开头不是",
    "10": "结尾不是",
    "11": "包含",
    "12": "不包含"
}*/

/**
 * 数据处理过滤器 ：获取操作符
 */
public class OperatorUtils {

    /**
     * mysql 操作符
     * @param num
     * @return
     */
    public static String getMysqlOperator(String num) {
        if ("1".equals(num)) {
            return ">";
        } else if ("2".equals(num)) {
            return "<";
        } else if ("3".equals(num)) {
            return "=";
        } else if ("4".equals(num)) {
            return ">=";
        } else if ("5".equals(num)) {
            return "<=";
        } else if ("6".equals(num)) {
            return "<>";
        } else if ("9".equals(num) || "10".equals(num) || "12".equals(num))  {
            return "not like"; // "9": "开头不是","10": "结尾不是","12": "不包含"
        } else {
            return "like";//"7": "开头是","8": "结尾是","11": "包含",
        }
    }



}
