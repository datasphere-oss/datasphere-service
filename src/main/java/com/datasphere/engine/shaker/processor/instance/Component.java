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

package com.datasphere.engine.shaker.processor.instance;

import com.datasphere.common.data.Dataset;
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
