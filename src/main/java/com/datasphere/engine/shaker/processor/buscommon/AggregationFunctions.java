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

package com.datasphere.engine.shaker.processor.buscommon;

public class AggregationFunctions {
    /*{
        "1": "COUNT"
        "2": "MAX"
        "3": "MIN"
        "4": "AVG"
        "5": "SUM"
    }*/
    public static String getAggregationFunction(String num) {
        if ("1".equals(num)) {
            return "COUNT";
        } else if ("2".equals(num)) {
            return "MAX";
        } else if ("3".equals(num)) {
            return "MIN";
        } else if ("4".equals(num)) {
            return "AVG";
        } else if ("5".equals(num)) {
            return "SUM";
        } else {
            return "no the aggregation function!";
        }
    }
}
