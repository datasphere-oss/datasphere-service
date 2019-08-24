package com.datasphere.engine.shaker.processor.buscommon;

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
 * 数据处理 Filter：获取操作符
 * jeq
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
