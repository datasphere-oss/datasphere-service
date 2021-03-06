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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
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
