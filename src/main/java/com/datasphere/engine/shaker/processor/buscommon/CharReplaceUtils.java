package com.datasphere.engine.shaker.processor.buscommon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CharReplaceUtils {

    /**
     * 把\n替换为空格
     * @param str
     * @return
     */
    public static String replace_N(String str) {
        return str.replaceAll("\\n", " ");
    }

    /**
     * 将sql字符串括号中的sql语句替换为新的sql语句
     */
    public static String replaceSql(String virtualDataset, String newSql) {
        JSONObject virtualDatasetObj = JSON.parseObject(virtualDataset);
        JSONObject dataset = virtualDatasetObj.getJSONObject("dataset");
        String sql = dataset.getString("sql");
        sql = sql.replaceAll("\\n", " ");
        String regex = "(?<=\\()[^\\)]+";//用于匹配()里面的文本
        newSql = newSql.replaceAll("\\n", " ");
        sql = sql.replaceAll(regex, newSql);
        dataset.put("sql", sql);
        virtualDatasetObj.put("dataset", dataset);
        return virtualDatasetObj.toString();
    }
}
