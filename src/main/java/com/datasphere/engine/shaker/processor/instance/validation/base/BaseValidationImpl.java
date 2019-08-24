package com.datasphere.engine.shaker.processor.instance.validation.base;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Singleton;

//@Service("baseValidation")
@Singleton
public class BaseValidationImpl implements IBaseValidation {
	private int defaultDecimal = 2;

	public int getDefaultDecimal() {
		return defaultDecimal;
	}

	public void setDefaultDecimal(int defaultDecimal) {
		this.defaultDecimal = defaultDecimal;
	}

	@Override
	public boolean validationFloat(String data, int num) {
		String reEx = "^[0-9]+(.[0-9]{1," + num + "})?$";
		return validation(data, reEx);
	}

	@Override
	public boolean validationNumber(String data) {
		String regEx = "^0|[1-9][0-9]*$";
		return validation(data, regEx);
	}

	@Override
	public boolean validationFloat(String data) {
		String reEx = "([1-9]+[0-9]*|0)(\\.[\\d]+)?";
		return validation(data, reEx);
	}

	@Override
	public boolean validationArray(String data) {

		return false;
	}

	@Override
	public Object changeType(Object data, ValidationChangType type) {

		switch (type) {
		case INT:
			return Integer.parseInt(data.toString());
		case FLOAT:
			return Float.parseFloat(data.toString());
		case BOOLEAN:
			return Boolean.parseBoolean(data.toString());
		default:
			return data;
		}
	}

	@Override
	public boolean validation(String data, String regEx) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(data);
		return matcher.matches();
	}

	public static void main(String[] args) {
		BaseValidationImpl b = new BaseValidationImpl();
		String str = "01";
		System.out.println(b.validationNumber(str));
	}

	@Override
	public boolean validationExciudeZeroNumber(String data) {
		String regEx = "^[1-9][0-9]*$";
		return validation(data, regEx);
	}

	@Override
	public boolean validationNotBlank(String data) {
		// TODO Auto-generated method stub
		return StringUtils.isNotBlank(data);
	}

	@Override
	public boolean validationIntegerInterval(ValidationParamsRole param) {
		int min = param.getMin(), max = param.getMax();
		Integer v = Integer.parseInt(param.getValue().toString());
		boolean b = false;
		if (param.isIncludeMin())
			b = v >= min;
		else
			b = v > min;
		if (!b) {
			return b;
		}
		if (param.isIncludeMax())
			b = v <= max;
		else
			b = v < max;
		if (!b)
			return b;
		return b;
	}

	@Override
	public boolean validationFloatInterval(ValidationParamsRole param) {
		float min = param.getMin(), max = param.getMax();
		float v = Float.parseFloat(param.getValue().toString());
		boolean b = false;
		if (param.isIncludeMin())
			b = v >= min;
		else
			b = v > min;
		if (!b) {
			return b;
		}
		if (param.isIncludeMax())
			b = v <= max;
		else
			b = v < max;
		if (!b)
			return b;
		return b;
	}

}
