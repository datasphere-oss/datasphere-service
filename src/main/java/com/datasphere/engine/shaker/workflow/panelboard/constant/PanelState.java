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

package com.datasphere.engine.shaker.workflow.panel.constant;

public class PanelState {
	public static String RUNNING = "0";//运行状态不能删除,其余状态都可以删除
	public static String SUCCESS = "1";
	public static String FAILURE = "2";
	public static String PREPARED = "3";//传入数据源默认状态
	public static String STOP = "4";
	public static String PAUSE = "5";
}
