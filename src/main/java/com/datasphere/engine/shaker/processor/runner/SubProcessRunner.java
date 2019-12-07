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

package com.datasphere.engine.shaker.processor.runner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.common.data.Dataset;
//import com.datasphere.engine.datasource.connections.jdbc.service.DataAccessor;
import com.datasphere.engine.shaker.processor.instance.AssociationEndpoint;
import com.datasphere.engine.shaker.processor.instance.Component;


public class SubProcessRunner extends ProcessRunner {
	private Map<String, Dataset> subProcessInputMap;
	private List<String> beginComponentIds;

	public SubProcessRunner(String panelId, String processId, DataAccessor dataAccessor) {
		super(panelId, processId, dataAccessor);
	}

	public void setBeginComponentIds(List<String> beginComponentIds) {
		this.beginComponentIds = beginComponentIds;
	}

	public void setInputMap(String name, Dataset dataset) {
		if (null == subProcessInputMap) {
			subProcessInputMap = new HashMap<>();
		}
		subProcessInputMap.put(name, dataset);
	}

	public void setAllInput(Map<String, Dataset> inputMap) {
		if (null == subProcessInputMap) {
			subProcessInputMap = new HashMap<>();
		}
		this.subProcessInputMap.putAll(inputMap);
	}
	
	@Override
	public void assembleInputDatasets(Component c) throws Exception {
		List<String> inputNames = c.getInputNames();
		for (String inputName : inputNames) {
			if(StringUtils.isBlank(inputName)) continue;
			Dataset dataset = null;
			if (beginComponentIds.contains(c.getId())) {
				dataset = subProcessInputMap.get(inputName);
			} else {
				AssociationEndpoint endpoint = c.getParentOutputEndpointByInputName(inputName);
				if (endpoint == null) {
					continue;
				}
				Component component = finishedComponentsMap.get(endpoint.getComponent().getId());

				if (component != null) {
					dataset = component.getOutput(endpoint.getName());
				} else {
					String pre_dataKey = endpoint.getComponent().getDataSetKey(endpoint.getName());
					dataset = dataAccessor.getDatasetMetadata(pre_dataKey);
				}
			}
			c.setInput(inputName, dataset);
		}

	}

}
