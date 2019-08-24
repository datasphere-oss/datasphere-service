package com.datasphere.engine.manager.resource.provider.database.config;

public class OracleDBConfig extends DBConfig {
	
	private String serviceName;
	
	private String serviceType;


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
}
