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

package com.datasphere.server.user;

import static com.datasphere.server.user.UserErrorCodes.USER_COMMON_ERROR_CODE;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.datasphere.server.common.exception.ErrorCodes;
import com.datasphere.server.common.exception.DSSException;

/**
 * Created by aladin on 2019. 5. 30..
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "User error.")
public class UserException extends DSSException {

  public UserException(String message) {
    super(USER_COMMON_ERROR_CODE, message);
  }

  public UserException(ErrorCodes errorCodes, String message) {
    super(errorCodes, message);
  }

  public UserException(String message, Throwable cause) {
    super(USER_COMMON_ERROR_CODE, message, cause);
  }

  public UserException(Throwable cause) {
    super(USER_COMMON_ERROR_CODE, cause);
  }
}
