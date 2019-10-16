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

package com.datasphere.engine.shaker.processor.instance.predatacomponent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.common.data.Dataset;
import com.datasphere.engine.core.utils.ObjectMapperUtils;
import com.datasphere.engine.shaker.processor.buscommon.utils.HttpUtils;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.engine.shaker.processor.instance.analysis.WarpInputAndOutput;
import com.datasphere.engine.shaker.processor.instance.callbackresult.ComponentCalcuateResult;
import com.datasphere.engine.shaker.processor.instance.componentparams.BaseComponentParams;
import com.datasphere.engine.shaker.processor.instance.componentparams.InputParams;
import com.datasphere.engine.shaker.processor.instance.componentparams.OutPutParams;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.message.status.notice.CallBackStatusMessage;
import com.datasphere.engine.shaker.workflow.panelboard.model.sub.PreDataProcessEntity;
import com.datasphere.server.common.exception.JIllegalOperationException;

/**
 * Title: PreDataComponentInstance
 * 预处理类
 * @date 2017年7月25日 上午9:25:08
 */
public class PreDataComponentInstance extends AbstractComponent {

	private final static Log logger = LogFactory.getLog(PreDataComponentInstance.class);
	private Map<String, Object> paramMap = new HashMap<>();
	private PreDataProcessEntity preDataProcessEntity;
	private static final String INPUT = "IN001";
	private static final String OUTPUT = "OUT001";
	private static final String OUTPUTPRE = "PRE001";
	private final List<String> columns = new ArrayList<>();

	public PreDataComponentInstance(ComponentInstance componentInstance) {
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

	/**
	 * 当预处理操作为空时，则将输入按原结果输出
	 * 
	 * @author kevin 2017年7月31日 下午6:10:46
	 * @throws SQLException
	 */
	public void preDataProcessEntityIsNull() throws SQLException {
		Dataset dataset = inputsMap.get(INPUT);
		// dataset = dataAccessor.getDatasetMetadata(dataset.getDataKey());
		// List<String> filedList = new ArrayList<>();
		// for (Column entity : dataset.getColumnsMeta()) {
		// filedList.add(entity.getName());
		// }
		updateParams(dataset.getDataKey());
		setAllOutputs(dataset);
	}

	/**
	 * 预处理操作不为空执行正常操作
	 * 
	 * @author kevin 2017年7月31日 下午6:15:40
	 * @throws Exception
	 */
	public void preDataProcessEntityIsNotNull() throws Exception {
		wrapParams();
		ComponentCalcuateResult calcuateResult = new ComponentCalcuateResult();
		calcuateResult.setStatus(ComponentInstanceStatus.RUNNING);
		CallBackStatusMessage.getInstance().add(jobId, calcuateResult);
		String url = String.valueOf(propertiesBean.getDefault("COMPONENTINSTANCE_CALCULATE_GROUPURL"));
		Map<String, String> data = new HashMap<>();
		String paramsStr = ObjectMapperUtils.writeValue(paramMap);
		logger.info("批处理组件参数===>" + paramsStr);
		data.put("params", paramsStr);
		String res = HttpUtils.post(url, data);
		if (!StringUtils.isBlank(res)) {
			JSONObject obj = JSONObject.parseObject(res);
			if (obj == null || !obj.containsKey("code") || 0 != obj.getIntValue("code")) {
				throw new JIllegalOperationException(String.valueOf(propertiesBean.get("CALCULATE_RESPONSE_ERROR")));
			}
		}
		logger.info("批处理组件运行响应结果===>" + res);
		calcuateResult = getStatusFromCSP(calcuateResult);

		if (calcuateResult.getStatus().equals(ComponentInstanceStatus.FAILURE)) {
			throw new JIllegalOperationException(calcuateResult.getMessage());
		}

		String preDataKey = getDataSetKey(OUTPUTPRE);
		String dataKey = getDataSetKey(OUTPUT);
		/**
		 * to do 按预处理的列输出重新复制一份数据
		 */
		String[] cls = new String[columns.size()];
		columns.toArray(cls);
		dataAccessor.saveasDataset(preDataKey, dataKey, cls);
		dataAccessor.removeDataset(preDataKey);
		Dataset dataset = new Dataset();
		dataset.setDataKey(dataKey);
		setAllOutputs(dataset);
	}

	@Override
	protected void compute() throws Exception {
		if(null==inputsMap) throw new JIllegalOperationException("没有数据输入！");
		preDataProcessEntity = this.getComponentInstancesFromPre();
		if (null == preDataProcessEntity || null == preDataProcessEntity.getOperates()) {
			preDataProcessEntityIsNull();
		} else {
			preDataProcessEntityIsNotNull();
		}

	}

	/**
	 * 从预处理项目中获取操作列表以及字段展示列表
	 * 
	 * @author kevin 2017年7月31日 下午6:18:40
	 * @return
	 * @throws Exception
	 */
	private PreDataProcessEntity getComponentInstancesFromPre() throws Exception {
		return preDataComponentService.getPreDataProcessEntity(this.getId(), this.componentInstance.getCreator());
	}

	/**
	 * 设置组件输入集
	 * 
	 * @author kevin 2017年7月31日 下午6:17:57
	 */
	private void setInputParams() {

		Dataset dataset = inputsMap.get(INPUT);
		InputParams inputParams = new InputParams();
		inputParams.setCode(INPUT);
		inputParams.setColumns(null);
		inputParams.setDataKey(dataset.getDataKey());
		paramMap.put("input", inputParams);
	}

	/**
	 * 设置组件输出集
	 * 
	 * @author kevin 2017年7月31日 下午6:18:21
	 */
	private String setOutPutParams() {
		OutPutParams outPutParams = new OutPutParams();
		String dataKey = getDataSetKey(OUTPUTPRE);
		outPutParams.setCode(OUTPUT);
		outPutParams.setDataKey(dataKey);
		paramMap.put("output", outPutParams);
		return dataKey;
	}

	@Override
	public String getDataSetKey(String name) {
		// TODO Auto-generated method stub
		return super.getDataSetKey(name);
	}

	@Override
	protected void wrapParams() throws Exception {
		setInputParams();
		setOutPutParams();
		setAlgms();
		setCiParams();
		String url = String.valueOf(propertiesBean.getDefault("COMPONENTINSTANCE_CALCULATE_CALLBACK_URL"));
		if (!url.endsWith("/"))
			url = url + "/";
		url = url + this.jobId;
		paramMap.put("callbackUrl", url);
		paramMap.put("code", componentInstance.getComponentType());
	}

	/**
	 * 设置预处理组件操作参数
	 * 
	 * @author kevin 2017年7月31日 下午6:17:32
	 * @throws Exception
	 */
	private void setAlgms() throws Exception {
		List<BaseComponentParams> operates = JSONArray.parseArray(preDataProcessEntity.getOperates(),
				BaseComponentParams.class);
		paramMap.put("algms", operates);
	}

	/**
	 * 将预处理操作最终的字段更新到组建参数中
	 * 
	 * @author kevin 2017年7月31日 下午6:16:58
	 */
	private void setCiParams() {
		List<PreDataFinalFiledEntity> fileds = JSONArray.parseArray(preDataProcessEntity.getColumns(),
				PreDataFinalFiledEntity.class);
		// List<String> filedList = new ArrayList<>();
		for (PreDataFinalFiledEntity entity : fileds) {
			columns.add(entity.getRealName());
		}
		// componentInstance.setCiParams(filedList);
		updateParams(getDataSetKey(OUTPUT));
	}

	/**
	 * 更新组件参数，为前端展示数据集提供查询字段参数
	 * 
	 * @author kevin 2017年7月31日 下午6:16:20
	 */
	// public void updateParams(List<String> filedList, String dataKey) {
	// ComponentInstance instance = new ComponentInstance();
	// instance.setId(getId());
	// instance.setCreator(componentInstance.getCreator());
	// HashMap<String, Object> p = new HashMap<>();
	// p.put("dataKey", dataKey);
	// p.put("columns", filedList);
	// instance.setCiParams(ObjectMapperUtils.writeValue(p));
	// componentInstanceService.update(instance);
	// }
	public void updateParams(String dataKey) {
		ComponentInstance instance = new ComponentInstance();
		instance.setId(getId());
		instance.setCreator(componentInstance.getCreator());
		HashMap<String, Object> p = new HashMap<>();
		p.put("dataKey", dataKey);
		instance.setCiParams(ObjectMapperUtils.writeValue(p));
		componentInstanceService.update(instance);
	}

}
