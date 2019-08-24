package com.datasphere.engine.core.utils;

import java.util.HashMap;
import java.util.Map;

public  class ExceptionConst {

	public static final Integer Success = 0;
	public static final Integer Failed = 1;
	public static final Integer DELETE_PANEL_EXISTS_RUNNING_EXCEPTION = 2;
	
	public static final Integer NAME_REPEAT = 11;
	public static final Integer NAMEORID_IS_NULL = 12;
	public static final Integer RECORD_NOT_EXITS=13;
	public static final Integer RUN_FAIL=14;
	public static final Integer PRIVATE_SROUCE_NOT_TEMPLAION=15;

	public static final Integer Unknown = 99;
	
	private static final Map<Integer, Object> CODE = new HashMap<Integer, Object>() {
		
		private static final long serialVersionUID = 1L;
		{
			put(Success, "调用成功！");
			put(Failed, "调用失败！");
			put(DELETE_PANEL_EXISTS_RUNNING_EXCEPTION, "存在正在运行的面板！无法删除！");
			put(NAME_REPEAT, "名称重复！");
			put(NAMEORID_IS_NULL, "名称和项目id为空");
			put(RECORD_NOT_EXITS, "记录不存在!");
			put(RUN_FAIL, "运行失败!");
			put(PRIVATE_SROUCE_NOT_TEMPLAION, "私有数据源不能制作模版！");
		}
	};

	public static Object get(Integer code) {
		return contains(code) ? CODE.get(code) : "未知错误";
	}

	public static boolean contains(Integer code) {
		return CODE.containsKey(code);
	}

}
