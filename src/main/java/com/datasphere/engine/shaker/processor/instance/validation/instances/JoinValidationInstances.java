package com.datasphere.engine.shaker.processor.instance.validation.instances;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.engine.shaker.processor.instance.validation.base.AbstractComponentValition;
import com.datasphere.engine.shaker.processor.instance.validation.base.ComponentParamsNameDescription;
import com.datasphere.engine.shaker.processor.instance.validation.base.ValidationChangType;
import com.datasphere.engine.shaker.processor.instance.validation.base.ValidationParamsRole;
import com.datasphere.resource.manager.module.dal.buscommon.utils.StringUtils;

import io.micronaut.core.util.CollectionUtils;

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
 * @author kevin
 * @date 2017年8月15日 下午3:50:32
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

//		JSONArray LeftOutput = JSONArray.parseArray(JSONArray.toJSONString(reqParams.get("LeftOutput")));
//		JSONArray RightOutput = JSONArray.parseArray(JSONArray.toJSONString(reqParams.get("RightOutput")));
//		if (CollectionUtils.isEmpty(LeftOutput) && CollectionUtils.isEmpty(RightOutput)) {
//			errorFlag = true;
//			errorMessage.append("组件[" + componentName + "]["+ComponentParamsNameDescription.JoinOutColumn+"]没有设置输出列！");
//		}
	}

}
