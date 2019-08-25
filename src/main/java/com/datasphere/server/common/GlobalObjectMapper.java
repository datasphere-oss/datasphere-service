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

package com.datasphere.server.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;

/**
 * Spring Context An ObjectMapper singleton class for use within classes that are not affected <br/>
 *   - Use getDefaultMapper() method when using the common ObjectMapper used in the whole application <br/>
 *   - If necessary, configure additional settings in Builder class by adding class properties <br/>
 */
@Component
public final class GlobalObjectMapper {

  private static ObjectMapper defaultMapper;

  private static ObjectMapper quoteNonNumericMapper;

  private static ObjectMapper resultSetMapper;

  private GlobalObjectMapper() {
    defaultMapper = getDefaultBuilder().build();
    resultSetMapper = getDefaultBuilder()
        .serializers(new ResultSetSerializer())
        .build();
    quoteNonNumericMapper = getDefaultBuilder()
        .featuresToDisable(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS)
        .build();
  }

  private static Jackson2ObjectMapperBuilder getDefaultBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
        .indentOutput(false)
        .createXmlMapper(false)
        .dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
        .failOnUnknownProperties(false)
        .featuresToEnable(ALLOW_NON_NUMERIC_NUMBERS)
        .featuresToEnable(ALLOW_SINGLE_QUOTES)
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        .modules(new JodaModule());
    return builder;
  }

  public static ObjectMapper getDefaultMapper() {
    if (defaultMapper == null) {
      defaultMapper = getDefaultBuilder().build();
    }

    return defaultMapper;
  }

  /**
   * For Logging or Debugging
   */
  public static String writeValueAsString(Object object) {
    if (defaultMapper == null) {
      defaultMapper = getDefaultBuilder().build();
    }

    if (object == null) {
      return null;
    }

    try {
      return defaultMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
    }

    return "";
  }

  public static String writeListValueAsString(Object object, Class<?> clazz) {
    if (defaultMapper == null) {
      defaultMapper = getDefaultBuilder().build();
    }

    if (object == null) {
      return null;
    }

    try {
      return defaultMapper.writerFor(defaultMapper.getTypeFactory()
                                                  .constructCollectionType(List.class, clazz))
                          .writeValueAsString(object);
    } catch (JsonProcessingException e) {
    }

    return "";
  }

  /**
   * For Logging or Debugging
   */
  public static <T> T readValue(String content, Class<T> valueType) {

    if (content == null) {
      return null;
    }

    if (defaultMapper == null) {
      defaultMapper = getDefaultBuilder().build();
    }

    try {
      return defaultMapper.readValue(content, valueType);
    } catch (IOException e) {
    }

    return null;
  }

  public static <T> List<T> readListValue(String content, Class<T> valueType) {
    if (defaultMapper == null) {
      defaultMapper = getDefaultBuilder().build();
    }

    try {
      return defaultMapper.readValue(content, defaultMapper.getTypeFactory()
                                                           .constructCollectionType(List.class, valueType));
    } catch (IOException e) {
    }

    return null;
  }

  public static <T> T readValue(String content, TypeReference<T> typeReference) {
    if (defaultMapper == null) {
      defaultMapper = getDefaultBuilder().build();
    }

    try {
      return defaultMapper.readValue(content, typeReference);
    } catch (IOException e) {
    }

    return null;
  }

  public static Map readValue(String content) {
    return readValue(content, Map.class);
  }

  public static ObjectMapper getResultSetMapper() {
    if (resultSetMapper == null) {
      resultSetMapper = getDefaultBuilder()
          .serializers(new ResultSetSerializer())
          .build();
    }

    return resultSetMapper;
  }

  public static ObjectMapper getQuoteNonNumericMapper() {
    if (quoteNonNumericMapper == null) {
      quoteNonNumericMapper = getDefaultBuilder()
          .featuresToDisable(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS)
          .build();
    }
    return quoteNonNumericMapper;
  }
}
