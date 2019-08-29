package com.datasphere.engine.common.exception;

import com.datasphere.engine.common.exception.ErrorCodes;
import com.datasphere.engine.common.exception.GlobalErrorCodes;
import com.google.common.base.Preconditions;

public class MetatronException extends RuntimeException {

	public static String DEFAULT_GLOBAL_MESSAGE = "Ooops!";

	  ErrorCodes code;

	  public MetatronException(ErrorCodes code, String message) {
	    this(code, message, null);
	  }

	  public MetatronException(ErrorCodes code, Throwable cause) {
	    this(code, cause.getMessage(), cause);
	  }

	  public MetatronException(ErrorCodes code, String message, Throwable cause) {
	    super(message, cause);
	    this.code = Preconditions.checkNotNull(code);
	  }

	  public MetatronException(String message) {
	    super(message);
	  }

	  public MetatronException(String message, Throwable cause) {
	    super(message, cause);
	  }

	  public MetatronException(Throwable cause) {
	    super(cause);
	  }

	  public ErrorCodes getCode() {
	    if(code == null) {
	      return GlobalErrorCodes.DEFAULT_GLOBAL_ERROR;
	    }

	    return code;
	  }
}