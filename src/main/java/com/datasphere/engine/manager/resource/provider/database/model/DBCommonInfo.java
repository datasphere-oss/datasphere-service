package com.datasphere.engine.manager.resource.provider.database.model;

import java.util.List;

import com.datasphere.engine.common.exception.JRuntimeException;

public class DBCommonInfo extends DBQuery implements Cloneable{
	
	private String host;
	private Integer port;
	private String user;
	private String password;
	private String databaseType;
	private String serviceName;
	private String serviceType;
	private String dataSourceJson;
	
	private List<DBDataSourceInfo> dataSources;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<DBDataSourceInfo> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DBDataSourceInfo> dataSources) {
		this.dataSources = dataSources;
	}

	public String getDataSourceJson() {
		return dataSourceJson;
	}

	public void setDataSourceJson(String dataSourceJson) {
		this.dataSourceJson = dataSourceJson;
	}

	public DBCommonInfo clone() {
		try {
			return (DBCommonInfo)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new JRuntimeException();
		}
	}
	
	@Override
	public String toString() {
		return "DBCommonInfo [host=" + host + ", port=" + port + ", user=" + user + ", password=" + password
				+ ", databaseType=" + databaseType + ", serviceName=" + serviceName + ", serviceType=" + serviceType
				+ ", dataSourceJson=" + dataSourceJson + ", dataSources=" + dataSources + ", getDatabaseName()="
				+ getDatabaseName() + ", getSchemaName()=" + getSchemaName() + ", getTableName()=" + getTableName()
				+ ", getPage()=" + getPage() + ", getRows()=" + getRows() + ", getSearchName()=" + getSearchName()
				+ "]";
	}

}
