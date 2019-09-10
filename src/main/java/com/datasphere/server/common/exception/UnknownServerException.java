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

package com.datasphere.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.datasphere.engine.common.exception.GlobalErrorCodes;
import com.datasphere.engine.common.exception.MetatronException;

/**
 * API 로직 처리중 예상하지 못한 오류 발생시 활용
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown server error")
public class UnknownServerException extends MetatronException {

  public UnknownServerException(String message) {
    super(GlobalErrorCodes.UNKNOWN_SERVER_ERROR_CODE, message);
  }

  public UnknownServerException(Throwable cause) {
    super(GlobalErrorCodes.UNKNOWN_SERVER_ERROR_CODE, cause);
  }

  public UnknownServerException(String message, Throwable cause) {
    super(GlobalErrorCodes.UNKNOWN_SERVER_ERROR_CODE, message, cause);
  }
}
