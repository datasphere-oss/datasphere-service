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

package com.datasphere.server.common.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;

/**
 * Created by aladin on 2019. 5. 10..
 */
public class JsonMapConverter implements AttributeConverter<Object, String> {

  private static final ObjectMapper om = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Object attribute) {

    try {
      return om.writeValueAsString(attribute);
    } catch (JsonProcessingException ex) {
      //log.error("Error while transforming Object to a text datatable column as json string", ex);
      return null;
    }
  }

  @Override
  public Object convertToEntityAttribute(String dbData) {
    try {
      if(dbData != null){
        return om.readValue(dbData, Object.class);
      }
      return dbData;
    } catch (IOException ex) {
      //log.error("IO exception while transforming json text column in Object property", ex);
      return null;
    }
  }
}
