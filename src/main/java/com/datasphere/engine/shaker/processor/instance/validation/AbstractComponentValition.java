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

package com.datasphere.engine.shaker.processor.instance.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;

public abstract class AbstractComponentValition {
	protected IBaseValidation baseValidation;

	protected List<ValidationParamsRole> ALLPARAMSROLES = new ArrayList<>();
	protected String componentName;
	protected HashMap<String, Object> reqParams;
	protected ComponentInstance instance;
	protected boolean errorFlag = false;
	protected StringBuilder errorMessage = new StringBuilder();

	public ComponentInstance getInstance() {
		return instance;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public StringBuilder getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(StringBuilder errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setInstance(ComponentInstance ci) {
		this.instance = ci;
		componentName = ci.getCiName();
		Object paramsStr = ci.getCiParams();
		if (null == paramsStr) {
			reqParams = new HashMap<>();
		} else {
			reqParams = (HashMap<String, Object>) paramsStr;
		}
		init();
		validation();
	}

	public void setReqParams(HashMap<String, Object> reqParams) {
		this.reqParams = reqParams;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public void setBaseValidation(IBaseValidation baseValidation) {
		this.baseValidation = baseValidation;
	}

	public abstract void init();
	
	public void validation() {
		for (int i = 0; i < ALLPARAMSROLES.size(); i++) {

			ValidationParamsRole param = ALLPARAMSROLES.get(i);
			String value = param.getValue() == null ? "" : param.getValue().toString();
			if (!param.isAllowEmpty()) {
				if (StringUtils.isBlank(value)) {
					param.setMessage("组件[" + componentName + "][" + param.getNameDesc() + "]为必填项！");
					errorFlag = true;
					errorMessage.append(param.getMessage());
					continue;
				}
			}
			if (param.getType() == ValidationChangType.INT) {
				if (param.isAllowEmpty() && StringUtils.isBlank(value))
					continue;
				if (!baseValidation.validationNumber(value)) {
					param.setMessage(
							"组件[" + componentName + "][" + param.getNameDesc() + "][" + param.getValue() + "]不是正整数！");
					errorFlag = true;
					errorMessage.append(param.getMessage());
					continue;
				}
			}
			if (param.getType() == ValidationChangType.FLOAT) {
				if (param.isAllowEmpty() && StringUtils.isBlank(value))
					continue;
				if (!baseValidation.validationFloat(value, param.getBit())) {
					param.setMessage(
							"组件[" + componentName + "][" + param.getNameDesc() + "][" + param.getValue() + "]不是浮点型！保留"+param.getBit()+"位小数！");
					errorFlag = true;
					errorMessage.append(param.getMessage());
					continue;
				}
			}
			

			param.setValue(baseValidation.changeType(value, param.getType()));
			reqParams.put(param.getName(), param.getValue());
			instance.setCiParams(reqParams);
			if (param.isValidationInterval()) {
				if (!baseValidation.validationFloatInterval(param)) {
					String pre = param.isIncludeMin() ? "[" : "(";
					String post = param.isIncludeMax() ? "]" : ")";
					param.setMessage("组件[" + componentName + "][" + param.getNameDesc() + "][" + param.getValue() + "]不在区间"
							+ pre + param.getMin() + "," + param.getMax() + post + "内！");
					errorMessage.append(param.getMessage());
					errorFlag = true;
				}
			}
		}
	}

}
