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
import com.google.common.base.Preconditions;

/**
 * API 또는 Resource 가 존재하지 않을 경우 활용
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
public class ResourceNotFoundException extends MetatronException {

  public ResourceNotFoundException(String resource) {
    this(resource, null);
  }

  public ResourceNotFoundException(Long resource) {
    this(resource, null);
  }

  public ResourceNotFoundException(Object resource, Throwable cause) {
    super(GlobalErrorCodes.NOT_FOUND_CODE, String.format("Resource(%s) not Found", Preconditions.checkNotNull(resource)), cause);
  }

  public ResourceNotFoundException(Throwable cause) {
    super(GlobalErrorCodes.NOT_FOUND_CODE, cause);
  }
}
