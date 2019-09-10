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

public class ValidationParamsRole {
	private String name;
	private Object value;
	private boolean isAllowEmpty = false;
	private ValidationChangType type;
	private Integer min = 0;
	private boolean isValidationInterval = false;
	private boolean isIncludeMin = true;
	private Integer max = Integer.MAX_VALUE;
	private boolean isIncludeMax = true;
	private StringBuilder message = new StringBuilder();
	private int bit = 2;
	private String nameDesc;

	public String getNameDesc() {
		return nameDesc;
	}

	public void setNameDesc(String nameDesc) {
		this.nameDesc = nameDesc;
	}

	public ValidationParamsRole(String name, boolean isAllowEmpty, ValidationChangType type) {
		super();
		this.name = name;
		this.isAllowEmpty = isAllowEmpty;
		this.type = type;
	}

	public ValidationParamsRole(String name, Object value,boolean isAllowEmpty, ValidationChangType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
		this.isAllowEmpty = isAllowEmpty;
	}

	

	/**
	 * 
	 * @param name key名称
	 * @param value 
	 * @param isAllowEmpty 是否为空
	 * @param type 值类型
	 * @param min 最小值
	 * @param isValidationInterval 是否验证区间
	 * @param isIncludeMin 是否包含最小指
	 * @param max 最大值
	 * @param isIncludeMax 是否包含最小值
	 * @param bit 保留小数位数
	 */
	public ValidationParamsRole(String name, Object value, boolean isAllowEmpty, ValidationChangType type, Integer min,
			boolean isValidationInterval, boolean isIncludeMin, Integer max, boolean isIncludeMax, int bit) {
		this.name = name;
		this.value = value;
		this.isAllowEmpty = isAllowEmpty;
		this.type = type;
		this.min = min;
		this.isValidationInterval = isValidationInterval;
		this.isIncludeMin = isIncludeMin;
		this.max = max;
		this.isIncludeMax = isIncludeMax;
		this.bit = bit;
	}
	

	public ValidationParamsRole() {
	}


	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public boolean isValidationInterval() {
		return isValidationInterval;
	}

	public void setValidationInterval(boolean isValidationInterval) {
		this.isValidationInterval = isValidationInterval;
	}

	public String getMessage() {
		return message.toString();
	}

	public void setMessage(String message) {
		this.message.append(message + "\n");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isAllowEmpty() {
		return isAllowEmpty;
	}

	public void setAllowEmpty(boolean isAllowEmpty) {
		this.isAllowEmpty = isAllowEmpty;
	}

	public ValidationChangType getType() {
		return type;
	}

	public void setType(ValidationChangType type) {
		this.type = type;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public boolean isIncludeMin() {
		return isIncludeMin;
	}

	public void setIncludeMin(boolean isIncludeMin) {
		this.isIncludeMin = isIncludeMin;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public boolean isIncludeMax() {
		return isIncludeMax;
	}

	public void setIncludeMax(boolean isIncludeMax) {
		this.isIncludeMax = isIncludeMax;
	}

}
