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

package com.datasphere.engine.common.exception;

import com.google.common.base.Preconditions;

public class DSSException extends RuntimeException {

  public static String DEFAULT_GLOBAL_MESSAGE = "Ooops!";

  ErrorCodes code;

  public DSSException(ErrorCodes code, String message) {
    this(code, message, null);
  }

  public DSSException(ErrorCodes code, Throwable cause) {
    this(code, cause.getMessage(), cause);
  }

  public DSSException(ErrorCodes code, String message, Throwable cause) {
    super(message, cause);
    this.code = Preconditions.checkNotNull(code);
  }

  public DSSException(String message) {
    super(message);
  }

  public DSSException(String message, Throwable cause) {
    super(message, cause);
  }

  public DSSException(Throwable cause) {
    super(cause);
  }

  public ErrorCodes getCode() {
    if(code == null) {
      return GlobalErrorCodes.DEFAULT_GLOBAL_ERROR;
    }

    return code;
  }
}
