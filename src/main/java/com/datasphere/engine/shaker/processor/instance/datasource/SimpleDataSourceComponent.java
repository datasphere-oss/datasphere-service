package com.datasphere.engine.shaker.processor.instance.datasource;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.common.dmpbase.data.Dataset;
import com.datasphere.engine.shaker.processor.instance.AbstractComponent;
import com.datasphere.resource.manager.module.component.instance.buscommon.analyse.WarpInputAndOutput;
import com.datasphere.resource.manager.module.component.instance.domain.ComponentInstance;

public class SimpleDataSourceComponent extends AbstractComponent {

	public SimpleDataSourceComponent(ComponentInstance componentInstance) {
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
		String dataKey = this.componentInstance.getComponentDefinitionId();
		Dataset dataset=new Dataset();
		dataset.setDataKey(dataKey);
//		Dataset dataset = dataAccessor.getDatasetMetadata(dataKey);
		setAllOutputs(dataset);
	}

	@Override
	protected void wrapParams() throws Exception {
		
	}

	@Override
	public void initOutDataSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
