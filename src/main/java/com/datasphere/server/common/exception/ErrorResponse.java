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

import java.io.Serializable;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.datasphere.engine.common.exception.ErrorCodes;
import com.datasphere.engine.common.exception.GlobalErrorCodes;
import com.datasphere.engine.common.exception.MetatronException;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.Preconditions;

/**
 * API 错误处理模型
 */
@JsonPropertyOrder({ "code", "message", "details" })
public class ErrorResponse implements Serializable {
  /**
   * 错误代码
   */
  String code;

  /**
   * 错误消息
   */
  String message;

  /**
   * 错误详情
   */
  Object details;

  public ErrorResponse() {
    // empty constructor
  }

  public ErrorResponse(ErrorCodes code, String message, Object details) {
    this.code = Preconditions.checkNotNull(code).getCode();
    this.message = message;
    this.details = details;
  }

  public ErrorResponse(MetatronException e) {
    this.code = e.getCode() == null ? GlobalErrorCodes.DEFAULT_GLOBAL_ERROR_CODE.toString() : e.getCode().toString();
    this.message = e.getMessage();
    this.details = ExceptionUtils.getStackTrace(e);
  }

  public static ErrorResponse unknownError(Exception ex) {
    return new ErrorResponse(GlobalErrorCodes.DEFAULT_GLOBAL_ERROR_CODE, MetatronException.DEFAULT_GLOBAL_MESSAGE, ExceptionUtils.getStackTrace(ex));
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getDetails() {
    return details;
  }

  public void setDetails(Object details) {
    this.details = details;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" +
        "code='" + code + '\'' +
        ", message='" + message + '\'' +
        ", details=" + details +
        '}';
  }
}
