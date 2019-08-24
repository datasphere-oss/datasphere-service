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

package com.datasphere.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 毫秒转yyyy-MM-dd hh:mm:ss
     * @param milliseconds
     */
    public static String millisecondsToDateTime(Long milliseconds) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(milliseconds == null || "".equals(milliseconds)){
            return "";
        }
        
        long anyTime = new Long(milliseconds);//将任意字符型的毫秒数转换成long型
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(anyTime);
        return df.format(calendar.getTime());
    }


    /**
     * yyyy-MM-dd hh:mm:ss转毫秒
     * @param date
     */
    public static Long dateTimeToMilliseconds(String date)throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sDt = sf.parse(date);
        return sDt.getTime() ;
    }

    /**
     * 获取当前时间yyyy-MM-dd hh:mm:ss
     */
    public static String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 唯一标识符生成
     */
    public static String getIdentifier(String og,String type) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return og+"-"+type+"-"+df.format(new Date());
    }


}
