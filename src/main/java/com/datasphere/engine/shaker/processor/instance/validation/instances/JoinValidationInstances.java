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

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.engine.shaker.processor.instance.validation.AbstractComponentValition;
import com.datasphere.engine.shaker.processor.instance.validation.ComponentParamsNameDescription;
import com.datasphere.engine.shaker.processor.instance.validation.ValidationChangType;
import com.datasphere.engine.shaker.processor.instance.validation.ValidationParamsRole;


/**
 * {"JoinCondition":"左连接","on":[{"r":"Survived","l":"label"}],"LeftOutput":[
 * "sepallength","sepalwidth","petallength","petalwidth","label"],"RightOutput":
 * []}
 * <p>
 * Title: JoinValidationInstances
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 */
public class JoinValidationInstances extends AbstractComponentValition {

	@Override
	public void init() {
		ValidationParamsRole JoinCondition = new ValidationParamsRole("JoinCondition", reqParams.get("JoinCondition"),
				false, ValidationChangType.STRING);
		JoinCondition.setNameDesc(ComponentParamsNameDescription.JoinCondition);
		ALLPARAMSROLES.add(JoinCondition);

	}

	@Override
	public void validation() {
		super.validation();
		JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(reqParams.get("on")));
		if (CollectionUtils.isEmpty(array)) {
			errorFlag = true;
			errorMessage.append("组件[" + componentName + "]["+ComponentParamsNameDescription.JoinOn+"] 没有设置关联条件！");
		} else {
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (!obj.containsKey("l") || StringUtils.isBlank(obj.getString("l")) || !obj.containsKey("r")
						|| StringUtils.isBlank(obj.getString("r"))) {
					errorFlag = true;
					errorMessage.append("组件[" + componentName + "]["+ComponentParamsNameDescription.JoinOn+"] 关联条件不完整！");
				}
			}
		}
	}

}
