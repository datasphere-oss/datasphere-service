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

package com.datasphere.engine.manager.resource.provider.config.model;

import java.util.List;

public class ElasticSearchConfig {
	private String userName;
	private String password;
	private List<ElasticSearchHost> hostList;
	private String authenticationType;
	private Boolean scriptsEnabled;
	private Boolean showHiddenIndices;
	private Boolean sslEnabled;
	private Boolean showIdColumn;
	private Integer readTimeoutMillis;
	private Integer scrollTimeoutMillis;
	private Boolean usePainless;
	private Boolean useWhitelist;
	private String fetchSize;


	public String getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(String fetchSize) {
		this.fetchSize = fetchSize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ElasticSearchHost> getHostList() {
		return hostList;
	}

	public void setHostList(List<ElasticSearchHost> hostList) {
		this.hostList = hostList;
	}

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public Boolean getScriptsEnabled() {
		return scriptsEnabled;
	}

	public void setScriptsEnabled(Boolean scriptsEnabled) {
		this.scriptsEnabled = scriptsEnabled;
	}

	public Boolean getShowHiddenIndices() {
		return showHiddenIndices;
	}

	public void setShowHiddenIndices(Boolean showHiddenIndices) {
		this.showHiddenIndices = showHiddenIndices;
	}

	public Boolean getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(Boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public Boolean getShowIdColumn() {
		return showIdColumn;
	}

	public void setShowIdColumn(Boolean showIdColumn) {
		this.showIdColumn = showIdColumn;
	}

	public Integer getReadTimeoutMillis() {
		return readTimeoutMillis;
	}

	public void setReadTimeoutMillis(Integer readTimeoutMillis) {
		this.readTimeoutMillis = readTimeoutMillis;
	}

	public Integer getScrollTimeoutMillis() {
		return scrollTimeoutMillis;
	}

	public void setScrollTimeoutMillis(Integer scrollTimeoutMillis) {
		this.scrollTimeoutMillis = scrollTimeoutMillis;
	}

	public Boolean getUsePainless() {
		return usePainless;
	}

	public void setUsePainless(Boolean usePainless) {
		this.usePainless = usePainless;
	}

	public Boolean getUseWhitelist() {
		return useWhitelist;
	}

	public void setUseWhitelist(Boolean useWhitelist) {
		this.useWhitelist = useWhitelist;
	}

	public Integer getScrollSize() {
		return scrollSize;
	}

	public void setScrollSize(Integer scrollSize) {
		this.scrollSize = scrollSize;
	}

	private Integer scrollSize;



}
