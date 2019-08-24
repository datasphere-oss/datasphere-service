package com.datasphere.engine.shaker.processor.instance;

import com.datasphere.common.dmpbase.data.Dataset;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;

import java.util.List;

public interface Component extends ComponentGetDataKeyMethod {

	String getId();

	String getName();

	ComponentClassification getComponentType();

	void setProcessId(String processId);

	List<Component> getParents();

	void run() throws Exception;

	void run(ProcessInstance instance) throws Exception;

	List<Component> getChildren();

	List<String> getInputNames();

	List<String> getOutputNames();

	void setInput(String name, Dataset dataset);

	Dataset getInput(String name);

	Dataset getOutput(String name);

	AssociationEndpoint getParentOutputEndpointByInputName(String inputName);

	Object getParamsStr();

	String getVersion();

	String getSubPanelId();

	String getStatus();

	void setStatus(String status);

	void setUserId(String userId);

	void init();

}
