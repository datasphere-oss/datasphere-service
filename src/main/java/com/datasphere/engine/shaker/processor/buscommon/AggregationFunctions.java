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
