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
