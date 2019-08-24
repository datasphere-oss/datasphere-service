package com.datasphere.engine.shaker.processor.runner;

import com.datasphere.resource.manager.module.dal.buscommon.utils.StringUtils;
import com.datasphere.resource.manager.module.dal.service.DataAccessor;
import com.datasphere.common.dmpbase.data.Dataset;
import com.datasphere.engine.shaker.processor.instance.AssociationEndpoint;
import com.datasphere.engine.shaker.processor.instance.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
