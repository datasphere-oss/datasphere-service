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

package com.datasphere.engine.shaker.processor.instance.model;

import com.datasphere.core.common.BaseEntity;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.model.ProcessRecord;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ComponentInstance extends BaseEntity {
	String ciName;
	String ciDescription;
	String componentDefinitionId;
	String panelId;
	String componentClassification;
	ComponentClassification componentType;
	String ciFrontPage;
	Object ciParams;
	String creator;
	String status;

	String version;//版本号
	Integer isSubProcess;//是否子流程
	String subPanelId;//子流程id
	Object inputName;//输入点名称，多个,逗号隔开
	Object outputName;//输出点名称，多个,逗号隔开

	public Object getInputName() {
		return inputName;
	}

	public void setInputName(Object inputName) {
		this.inputName = inputName;
	}

	public Object getOutputName() {
		return outputName;
	}

	public void setOutputName(Object outputName) {
		this.outputName = outputName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getIsSubProcess() {
		return isSubProcess;
	}

	public void setIsSubProcess(Integer isSubProcess) {
		this.isSubProcess = isSubProcess;
	}

	public String getSubPanelId() {
		return subPanelId;
	}

	public void setSubPanelId(String subPanelId) {
		this.subPanelId = subPanelId;
	}

	@JsonInclude(value = Include.NON_NULL)
	ProcessRecord processRecord;

	public ComponentInstance() {

	}

	public ComponentInstance(String panelId) {
		this.panelId = panelId;
	}

	public String getCiName() {
		return ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	public String getCiDescription() {
		return ciDescription;
	}

	public void setCiDescription(String ciDescription) {
		this.ciDescription = ciDescription;
	}

	public String getComponentDefinitionId() {
		return componentDefinitionId;
	}

	public void setComponentDefinitionId(String componentDefinitionId) {
		this.componentDefinitionId = componentDefinitionId;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public String getCiFrontPage() {
		return ciFrontPage;
	}

	public void setCiFrontPage(String ciFrontPage) {
		this.ciFrontPage = ciFrontPage;
	}

	public Object getCiParams() {
		return ciParams;
	}

	public void setCiParams(Object ciParams) {
		this.ciParams = ciParams;
	}

	public String getComponentClassification() {
		return componentClassification;
	}

	public void setComponentClassification(String componentClassification) {
		this.componentClassification = componentClassification;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public ComponentClassification getComponentType() {
		return componentType;
	}

	public void setComponentType(ComponentClassification componentType) {
		this.componentType = componentType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProcessRecord getProcessRecord() {
		return processRecord;
	}

	public void setProcessRecord(ProcessRecord processRecord) {
		this.processRecord = processRecord;
	}

}
