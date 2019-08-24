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
