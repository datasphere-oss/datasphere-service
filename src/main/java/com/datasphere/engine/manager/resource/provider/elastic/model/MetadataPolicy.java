package com.datasphere.engine.manager.resource.provider.elastic.model;

public class MetadataPolicy {
	private Integer authTTLMs;
	// 数据集刷新
	private Integer datasetRefreshAfterMs;
	// 数据集超时
	private Integer datasetExpireAfterMs;
	// 名称刷新
	private Integer namesRefreshMs;
	// 数据集刷新
	private String datasetUpdateMode;


	public Integer getAuthTTLMs() {
		return authTTLMs;
	}

	public void setAuthTTLMs(Integer authTTLMs) {
		this.authTTLMs = authTTLMs;
	}

	public Integer getDatasetRefreshAfterMs() {
		return datasetRefreshAfterMs;
	}

	public void setDatasetRefreshAfterMs(Integer datasetRefreshAfterMs) {
		this.datasetRefreshAfterMs = datasetRefreshAfterMs;
	}

	public Integer getDatasetExpireAfterMs() {
		return datasetExpireAfterMs;
	}

	public void setDatasetExpireAfterMs(Integer datasetExpireAfterMs) {
		this.datasetExpireAfterMs = datasetExpireAfterMs;
	}

	public Integer getNamesRefreshMs() {
		return namesRefreshMs;
	}

	public void setNamesRefreshMs(Integer namesRefreshMs) {
		this.namesRefreshMs = namesRefreshMs;
	}

	public String getDatasetUpdateMode() {
		return datasetUpdateMode;
	}

	public void setDatasetUpdateMode(String datasetUpdateMode) {
		this.datasetUpdateMode = datasetUpdateMode;
	}
}
