package com.datasphere.engine.shaker.processor.instance.defaultcomponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datasphere.resource.manager.module.dal.buscommon.utils.StringUtils;
import com.datasphere.common.dmpbase.data.Dataset;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.shaker.processor.buscommon.utils.HttpUtils;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.engine.shaker.processor.instance.analyse.WarpInputAndOutput;
import com.datasphere.engine.shaker.processor.instance.callbackresult.ComponentCalcuateResult;
import com.datasphere.engine.shaker.processor.instance.componentparams.DefaultComponentParams;
import com.datasphere.engine.shaker.processor.instance.componentparams.InputParams;
import com.datasphere.engine.shaker.processor.instance.componentparams.OutPutParams;
import com.datasphere.engine.shaker.processor.message.status.notice.CallBackStatusMessage;
import com.datasphere.resource.manager.module.component.instance.buscommon.constant.ComponentInstanceStatus;
import com.datasphere.resource.manager.module.component.instance.domain.ComponentInstance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DefaultComponentInstance extends AbstractComponent {
	private final static Log logger = LogFactory.getLog(DefaultComponentInstance.class);
	// private static final String INPUT_NAME = "IN001";
	// private static final String OUTPUT_NAME = "OUT001";

	public DefaultComponentInstance(ComponentInstance componentInstance) {
		super(componentInstance);
		if (OUTPUT_NAMES.isEmpty()) {
			String outputNameArray = JSONArray.toJSONString(componentInstance.getOutputName());
			List<WarpInputAndOutput> output = JSONObject.parseArray(outputNameArray, WarpInputAndOutput.class);
			if (null != output) {
				for (WarpInputAndOutput warp : output) {
					OUTPUT_NAMES.add(warp.getCode());
				}
			}
		}
		if (INPUT_NAMES.isEmpty()) {
			String inputNameArray = JSONArray.toJSONString(componentInstance.getInputName());
			List<WarpInputAndOutput> input = JSONObject.parseArray(inputNameArray, WarpInputAndOutput.class);
			if (input != null) {
				for (WarpInputAndOutput warp : input) {
					INPUT_NAMES.add(warp.getCode());
				}
			}
		}
	}

	@Override
	protected void compute() throws Exception {
		wrapParams();
		ComponentCalcuateResult calcuateResult = new ComponentCalcuateResult();
		calcuateResult.setStatus(ComponentInstanceStatus.RUNNING);
		CallBackStatusMessage.getInstance().add(jobId, calcuateResult);
		logger.info("组件参数===>" + this.params.toString());
		String url = String.valueOf(propertiesBean.getDefault("COMPONENTINSTANCE_CALCULATE_URL"));// jeq
		Map<String, String> data = new HashMap<>();
		data.put("params", this.params.toString());
		String res = HttpUtils.post(url, data);

		if (!StringUtils.isBlank(res)) {
			JSONObject obj = JSONObject.parseObject(res);
			if (obj == null || !obj.containsKey("code") || 0 != obj.getIntValue("code")) {
				throw new JIllegalOperationException(String.valueOf(propertiesBean.get("CALCULATE_RESPONSE_ERROR")));
			}
		}
		logger.info("计算平台响应结果===>" + res);
		calcuateResult = getStatusFromCSP(calcuateResult);

		if (calcuateResult.getStatus().equals(ComponentInstanceStatus.FAILURE)) {
			throw new JIllegalOperationException(calcuateResult.getMessage());

		}

		for (String name : OUTPUT_NAMES) {
			if (StringUtils.isBlank(name))
				continue;
			String dataKey = getDataSetKey(name);
			Dataset dataset = new Dataset();
			dataset.setDataKey(dataKey);
			// Dataset dataset = dataAccessor.getDatasetMetadata(dataKey);
			setOutput(name, dataset);
		}
	}

	@Override
	protected void wrapParams() throws Exception {
		if(null==inputsMap) throw new JIllegalOperationException("没有数据输入！");
		params = new DefaultComponentParams();
		List<InputParams> in = new ArrayList<>();
		for (String name : inputsMap.keySet()) {
			if (!StringUtils.isBlank(name)) {
				Dataset dataset = getInput(name);
				if (null != dataset) {
					InputParams inputParams = new InputParams();
					inputParams.setCode(name);
					inputParams.setColumns(null);
					inputParams.setDataKey(dataset.getDataKey());
					in.add(inputParams);
				}

			}

		}
		params.setInputs(in);
		Object attrs = getParamsStr();

		params.setAlgmAttrs(attrs);
		params.setCode(getComponentType().name());

		List<OutPutParams> outputs = new ArrayList<>();

		for (String name : getOutputNames()) {
			if (StringUtils.isBlank(name))
				continue;
			OutPutParams outPutParams = new OutPutParams();
			String dataKey = getDataSetKey(name);
			outPutParams.setCode(name);
			outPutParams.setDataKey(dataKey);
			outputs.add(outPutParams);

		}
		String url = String.valueOf(propertiesBean.getDefault("COMPONENTINSTANCE_CALCULATE_CALLBACK_URL"));// jeq
		if (!url.endsWith("/"))
			url = url + "/";
		url = url + this.jobId;
		params.setCallBackUrl(url);
		params.setCode(getComponentType().name());
		params.setOutputs(outputs);
	}

	
}
