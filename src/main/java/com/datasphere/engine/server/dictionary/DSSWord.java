package com.datasphere.engine.server.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.datasphere.core.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class DSSWord extends BaseEntity {
	
	public String name;
	
	public String code;
	
	@JsonInclude(value=Include.NON_NULL)
	public String parent;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	public String group;
	
	@JsonInclude(value=Include.NON_NULL)
	public String reserve;
	
	@SuppressWarnings("unchecked")
	public List<Object> children = Collections.EMPTY_LIST;
	
	@JsonInclude(value=Include.NON_NULL)
	public Integer nodeType;

	public DSSWord() {
		
	}
	
	public DSSWord(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public List<Object> getChildren() {
		return children;
	}

	public void setChildren(List<Object> children) {
		this.children = children;
	}
	
	public void addChild(Object child) {
		if(children == null || children == Collections.EMPTY_LIST) {
			children = new ArrayList<Object>();
		}
		children.add(child);
	}

	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
}
