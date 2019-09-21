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

import java.io.IOException;

import com.datasphere.server.common.exception.ErrorResponse;
import com.datasphere.server.common.exception.GlobalErrorCodes;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomOAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

  public CustomOAuth2ExceptionSerializer() {
    this(null);
  }

  public CustomOAuth2ExceptionSerializer(Class<CustomOAuth2Exception> t) {
    super(t);
  }

  @Override
  public void serialize(CustomOAuth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {

    ErrorResponse errorResponse = value.getErrorResponse();
    if(errorResponse != null) {
      gen.writeStartObject();
      gen.writeStringField("code", errorResponse.getCode());
      gen.writeStringField("message", errorResponse.getMessage());
      gen.writeStringField("details", (String) errorResponse.getDetails());
      gen.writeEndObject();
    } else {
      gen.writeStartObject();
      gen.writeStringField("code", GlobalErrorCodes.AUTH_ERROR_CODE.getCode());
      gen.writeStringField("message", value.getOAuth2ErrorCode());
      gen.writeStringField("details", value.getLocalizedMessage());
      gen.writeEndObject();
    }
  }
}
