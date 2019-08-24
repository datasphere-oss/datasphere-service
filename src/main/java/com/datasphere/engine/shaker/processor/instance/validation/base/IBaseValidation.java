package com.datasphere.engine.shaker.processor.instance.validation.base;

public interface IBaseValidation {

	boolean validationNotBlank(String data);

	boolean validationNumber(String data);

	boolean validationExciudeZeroNumber(String data);

	boolean validationFloat(String data);

	boolean validationFloat(String data, int num);

	boolean validationArray(String data);

	boolean validation(String data, String regEx);

	Object changeType(Object data, ValidationChangType type);

	boolean validationIntegerInterval(ValidationParamsRole role);

	boolean validationFloatInterval(ValidationParamsRole role);
}
