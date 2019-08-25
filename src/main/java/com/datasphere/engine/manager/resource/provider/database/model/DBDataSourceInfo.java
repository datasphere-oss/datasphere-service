package com.datasphere.engine.manager.resource.provider.database.model;

public class DBDataSourceInfo {

	private String tableName;
	private String datasourceClass="001001";
	private String datasourceName;
	private String datasourceDesc;

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	public String getDatasourceDesc() {
		return datasourceDesc;
	}
	public void setDatasourceDesc(String datasourceDesc) {
		this.datasourceDesc = datasourceDesc;
	}
	public String getDatasourceClass() {
		return datasourceClass;
	}
	public void setDatasourceClass(String datasourceClass) {
		this.datasourceClass = datasourceClass;
	}
	@Override
	public String toString() {
		return "HiveDataSourceInfo [tableName=" + tableName + ", datasourceClass=" + datasourceClass + ", datasourceName="
				+ datasourceName + ", datasourceDesc=" + datasourceDesc + "]";
	}

	
}
