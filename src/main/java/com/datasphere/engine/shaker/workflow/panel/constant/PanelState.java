package com.datasphere.engine.shaker.workflow.panel.constant;

public class PanelState {
	public static String RUNNING = "0";//运行状态不能删除,其余状态都可以删除
	public static String SUCCESS = "1";
	public static String FAILURE = "2";
	public static String PREPARED = "3";//传入数据源默认状态
	public static String STOP = "4";
	public static String PAUSE = "5";
}
