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

public enum GlobalErrorCodes implements ErrorCodes{

  DEFAULT_GLOBAL_ERROR_CODE("GB0000"),      // status 500
  UNKNOWN_SERVER_ERROR_CODE("GB0001"),      // status 500
  NOT_FOUND_CODE("GB0002"),                 // status 404
  BAD_REQUEST_CODE("GB0003"),               // status 400
  ACCESS_DENIED_CODE("GB0004"),             // status 403
  AUTH_ERROR_CODE("GB0005"),                // status 401
  INVALID_USERNAME_PASSWORD_CODE("GB0006"), // status 400
  INVALID_TOKEN_CODE("GB0007"),             // status 401

  DEFAULT_GLOBAL_ERROR("error.global.default");

  String errorCode;

  GlobalErrorCodes(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getCode() {
    return errorCode;
  }
}
