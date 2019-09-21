package com.datasphere.engine.manager.resource.provider.exception;

import java.sql.SQLException;

import com.datasphere.server.common.exception.JRuntimeException;

public class JSQLException extends JRuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String SQLState;
	
	private int vendorCode;
	
	public JSQLException(SQLException e) {
		super(e.getMessage());
		setErrorCode(1);
		setSQLState(e.getSQLState());
		setVendorCode(e.getErrorCode());
	}

	public String getSQLState() {
		return SQLState;
	}

	public void setSQLState(String sQLState) {
		SQLState = sQLState;
	}

	public int getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(int vendorCode) {
		this.vendorCode = vendorCode;
	}
}
