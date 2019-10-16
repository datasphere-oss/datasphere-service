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

public class ReturnMessageUtils {
    public static String IsNull = "";

    public static String ParameterIsNull = ":参数不能为空！";
    public static String ProcessRunFaild = ":运行工作流-失败！";
    public static String CancelJobFaild = ":任务取消-失败！";

    public static String ComponentIsNull = "工作流中组件实例为空";
    public static String OneTableProcessFaild = "单表处理-失败";
    public static String OneTableFilterFaild = "单表过滤Filter-失败";
    public static String MoreTableJoinFaild = "多表Join-失败";
    public static String MoreTableMixFaild = "多表混合处理-失败";



    public static String ComponentInstanceResultsIsNull = "当前组件实例结果集为空，请先运行此工作流！";
    public static String ComponentInstanceResultsIsNotNull = "当前组件实例结果集存在";

    public static String DaasAuthorizationInvalid = "token失效，请check!";


}
