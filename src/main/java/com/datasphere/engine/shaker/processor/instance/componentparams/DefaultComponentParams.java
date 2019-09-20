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

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class DefaultComponentParams extends BaseComponentParams{
	
	/**
	 * 组件输入
	 */
	private List<InputParams> inputs;
	/**
	 * 组件输出
	 */
	private List<OutPutParams> outputs;
	/**
	 * 组件回调地址
	 */
	@JSONField(name = "callbackUrl")
	private String callBackUrl;


	public List<InputParams> getInputs() {
		return inputs;
	}

	public void setInputs(List<InputParams> inputs) {
		this.inputs = inputs;
	}

	public List<OutPutParams> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<OutPutParams> outputs) {
		this.outputs = outputs;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSONObject.toJSONString(this);
	}
}
