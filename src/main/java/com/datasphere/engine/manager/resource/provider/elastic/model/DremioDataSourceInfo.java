package com.datasphere.engine.manager.resource.provider.elastic.model;

import java.util.List;

public class DremioDataSourceInfo {
	private String id;
	private String daasId;
	private String name;
	private String description;
	private String type;
	private Object config;
	private MetadataPolicy metadataPolicy;
	private Integer accelerationRefreshPeriodMs;
	private Integer accelerationGracePeriodMs;
	private String authenticationType;
	private List<Table> tables;
	private String businessType;
	private String spareType;

	public String getSpareType() {
		return spareType;
	}

	public void setSpareType(String spareType) {
		this.spareType = spareType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public String getDaasId() {
		return daasId;
	}

	public void setDaasId(String daasID) {
		this.daasId = daasID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getConfig() {
		return config;
	}

	public void setConfig(Object config) {
		this.config = config;
	}

	public MetadataPolicy getMetadataPolicy() {
		return metadataPolicy;
	}

	public void setMetadataPolicy(MetadataPolicy metadataPolicy) {
		this.metadataPolicy = metadataPolicy;
	}

	public Integer getAccelerationRefreshPeriodMs() {
		return accelerationRefreshPeriodMs;
	}

	public void setAccelerationRefreshPeriodMs(Integer accelerationRefreshPeriodMs) {
		this.accelerationRefreshPeriodMs = accelerationRefreshPeriodMs;
	}

	public Integer getAccelerationGracePeriodMs() {
		return accelerationGracePeriodMs;
	}

	public void setAccelerationGracePeriodMs(Integer accelerationGracePeriodMs) {
		this.accelerationGracePeriodMs = accelerationGracePeriodMs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
}
