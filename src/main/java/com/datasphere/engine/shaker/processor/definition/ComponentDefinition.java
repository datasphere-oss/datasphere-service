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

package com.datasphere.engine.shaker.processor.definition;

import com.datasphere.core.common.BaseEntity;

public class ComponentDefinition extends BaseEntity {
	private String id;		// 组件唯一标识
	private String code;		// 组件代码
	private String name;		// 组件名称
	private String classification;	//组件分类. 例：数据源、数据处理、机器学习等
	private String businessType;
	private String creator;
	private String dataConfig;
	private String icon;
	private Object in_point;
	private Object out_point;
	private Object params;
	private Integer nodeType;
	private String dataFrom;

	public String getBusineesType() {
		return businessType;
	}

	public void setBusineesType(String busineesType) {
		this.businessType = busineesType;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getDataConfig() {
		return dataConfig;
	}

	public void setDataConfig(String dataConfig) {
		this.dataConfig = dataConfig;
	}

	public ComponentDefinition() {
		
	}
	
	public ComponentDefinition(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public ComponentDefinition(String classification) {
		this.classification = classification;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	public Object getIn_point() {
		return in_point;
	}
	public void setIn_point(Object in_point) {
		this.in_point = in_point;
	}
	public Object getOut_point() {
		return out_point;
	}
	public void setOut_point(Object out_point) {
		this.out_point = out_point;
	}
}
