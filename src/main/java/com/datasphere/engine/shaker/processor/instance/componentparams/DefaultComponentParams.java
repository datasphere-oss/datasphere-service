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
