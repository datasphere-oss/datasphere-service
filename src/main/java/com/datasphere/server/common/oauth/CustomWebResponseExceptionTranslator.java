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

package com.datasphere.server.common.oauth;

import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.INVALID_GRANT;
import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.INVALID_TOKEN;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

import com.datasphere.server.common.exception.ErrorResponse;
import com.datasphere.server.common.exception.GlobalErrorCodes;

public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

  @Override
  public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

    ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);

    HttpHeaders headers = new HttpHeaders();
    headers.setAll(responseEntity.getHeaders().toSingleValueMap());

    ErrorResponse errorResponse;
    OAuth2Exception oAuth2Exception = responseEntity.getBody();
    switch (oAuth2Exception.getOAuth2ErrorCode()) {
      case INVALID_TOKEN:
        errorResponse = new ErrorResponse(GlobalErrorCodes.INVALID_TOKEN_CODE,
                                          oAuth2Exception.getOAuth2ErrorCode(),
                                          oAuth2Exception.getMessage());
        break;
      case INVALID_GRANT:
        errorResponse = new ErrorResponse(GlobalErrorCodes.INVALID_USERNAME_PASSWORD_CODE,
                                          oAuth2Exception.getOAuth2ErrorCode(),
                                          oAuth2Exception.getMessage());
        break;
      default:
        errorResponse = new ErrorResponse(GlobalErrorCodes.AUTH_ERROR_CODE,
                                          oAuth2Exception.getOAuth2ErrorCode(),
                                          oAuth2Exception.getMessage());
    }

    return new ResponseEntity<>(new CustomOAuth2Exception(errorResponse, oAuth2Exception), headers, responseEntity.getStatusCode());
  }
}
