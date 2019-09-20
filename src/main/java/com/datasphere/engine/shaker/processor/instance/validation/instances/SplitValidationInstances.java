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

package com.datasphere.engine.shaker.processor.instance.validation.instances;

import com.datasphere.engine.shaker.processor.instance.validation.AbstractComponentValition;
import com.datasphere.engine.shaker.processor.instance.validation.ComponentParamsNameDescription;
import com.datasphere.engine.shaker.processor.instance.validation.ValidationChangType;
import com.datasphere.engine.shaker.processor.instance.validation.ValidationParamsRole;

/**
 * {"percentage":0.8,"Stratified":false,"AttributedIndices":"","method":"fit"}
 * <p>
 * Title: SplitValidationInstances
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 */
public class SplitValidationInstances extends AbstractComponentValition {

	@Override
	public void init() {
		ValidationParamsRole percentage = new ValidationParamsRole("percentage", reqParams.get("percentage"), false,
				ValidationChangType.FLOAT, 0, true, false, 1, false, 2);
		percentage.setNameDesc(ComponentParamsNameDescription.split_percentage);
		ALLPARAMSROLES.add(percentage);

		ValidationParamsRole Stratified = new ValidationParamsRole("Stratified", reqParams.get("Stratified"), false,
				ValidationChangType.BOOLEAN);
		
		ALLPARAMSROLES.add(Stratified);

		boolean s = Boolean.parseBoolean(reqParams.get("Stratified").toString());
		if (s) {
			ValidationParamsRole AttributedIndices = new ValidationParamsRole("AttributedIndices",
					reqParams.get("AttributedIndices"), false, ValidationChangType.STRING);
			AttributedIndices.setNameDesc(ComponentParamsNameDescription.split_AttributedIndices);
			ALLPARAMSROLES.add(AttributedIndices);
		}
	}

}
