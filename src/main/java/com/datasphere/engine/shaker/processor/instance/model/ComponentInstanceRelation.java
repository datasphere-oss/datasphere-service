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


public class ComponentInstanceRelation extends BaseEntity {
//	@NotBlank(groups = { CreateComponentInstanceRelation.class }, message = "{CI.CDID.NOTBLANK}")
	private String sourceComponentInstanceId; // 源组件实例ID
//	@NotBlank(groups = { CreateComponentInstanceRelation.class }, message = "{CRI.NODE.NAME.NOTBLANK}")
	private String sourceOutputName; // 源输出点名称
	private String sourceDataKey;
//	@NotBlank(groups = { CreateComponentInstanceRelation.class }, message = "{CI.CDID.NOTBLANK}")
	private String destComponentInstanceId; // 目的组件实例ID
//	@NotBlank(groups = { CreateComponentInstanceRelation.class }, message = "{CRI.NODE.NAME.NOTBLANK}")
	private String destInputName; // 目的输入点名称
	private String frontPage; // 前端扩展属性
	private String panelId;
	private String creator;
	private Integer instanceRelation;
	private Integer orderNum;

	public String getSourceDataKey() {
		return sourceDataKey;
	}

	public void setSourceDataKey(String sourceDataKey) {
		this.sourceDataKey = sourceDataKey;
	}

	public Integer getInstanceRelation() {
		return instanceRelation;
	}

	public void setInstanceRelation(Integer instanceRelation) {
		this.instanceRelation = instanceRelation;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public ComponentInstanceRelation() {
	}

	public ComponentInstanceRelation(String panelId) {
		this.panelId = panelId;
	}

	public ComponentInstanceRelation(String id, String frontPage) {
		setId(id);
		setFrontPage(frontPage);
	}

	public String getSourceComponentInstanceId() {
		return sourceComponentInstanceId;
	}

	public void setSourceComponentInstanceId(String sourceComponentInstanceId) {
		this.sourceComponentInstanceId = sourceComponentInstanceId;
	}

	public String getDestComponentInstanceId() {
		return destComponentInstanceId;
	}

	public void setDestComponentInstanceId(String destComponentInstanceId) {
		this.destComponentInstanceId = destComponentInstanceId;
	}

	public String getFrontPage() {
		return frontPage;
	}

	public void setFrontPage(String frontPage) {
		this.frontPage = frontPage;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public String getSourceOutputName() {
		return sourceOutputName;
	}

	public void setSourceOutputName(String sourceOutputName) {
		this.sourceOutputName = sourceOutputName;
	}

	public String getDestInputName() {
		return destInputName;
	}

	public void setDestInputName(String destInputName) {
		this.destInputName = destInputName;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}
