package com.datasphere.engine.common.exception;

public class MetatronException extends Exception{
	public static String DEFAULT_GLOBAL_MESSAGE = "Ooops!";
	
	ErrorCodes codes;
	
	public MetatronException(String message) {
		
	}

	public MetatronException(ErrorCodes codes, String message) {
		this.codes = codes;
	}
	
	public MetatronException(ErrorCodes codes, Throwable cause) {
		this.codes = codes;
	}

	public MetatronException(ErrorCodes codes, String message, Throwable cause) {
		this.codes = codes;
	}

	public MetatronException(String message, Throwable cause) {
		
	}
	
	public ErrorCodes getCode() {
		return this.codes;
	}
	
}
