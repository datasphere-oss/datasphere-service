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

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class BadRequestException extends MetatronException {

  public BadRequestException(String message) {
    super(GlobalErrorCodes.BAD_REQUEST_CODE, message);
  }

  public BadRequestException(Throwable cause) {
    super(GlobalErrorCodes.BAD_REQUEST_CODE, cause);
  }

  public BadRequestException(String message, Throwable cause) {
    super(GlobalErrorCodes.BAD_REQUEST_CODE, message, cause);
  }
}
