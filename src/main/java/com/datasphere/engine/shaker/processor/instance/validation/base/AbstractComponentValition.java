package com.datasphere.engine.shaker.processor.instance.validation.base;

import com.datasphere.resource.manager.module.dal.buscommon.utils.StringUtils;
import com.datasphere.resource.manager.module.component.instance.domain.ComponentInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	/**
	 * 初始化规则
	 * 
	 * @author kevin 2017年8月15日 下午12:47:04
	 */
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
