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

package com.datasphere.engine.shaker.processor.instance.componentparams;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseComponentParams implements AbstractMethodParams{
	
	/**
	 * 组件编码
	 */
	private String code;
	/**
	 * 组件参数
	 */
	@JSONField(name = "algmAttrs")
	private Object algmAttrs;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getAlgmAttrs() {
		return algmAttrs;
	}
	public void setAlgmAttrs(Object algmAttrs) {
		this.algmAttrs = algmAttrs;
	}
	
}
