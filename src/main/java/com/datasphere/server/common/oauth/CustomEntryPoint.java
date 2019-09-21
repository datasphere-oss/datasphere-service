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

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.annotation.PostConstruct;

public class CustomEntryPoint extends OAuth2AuthenticationEntryPoint {

  @Autowired
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  private CustomWebResponseExceptionTranslator exceptionTranslator;

  public CustomEntryPoint() {
  }

  @PostConstruct
  public void init() {
    // JSON Type Converter for passing the result of
    DefaultOAuth2ExceptionRenderer renderer = new DefaultOAuth2ExceptionRenderer();
    renderer.setMessageConverters(Lists.newArrayList(mappingJackson2HttpMessageConverter));
    setExceptionRenderer(renderer);
    setExceptionTranslator(exceptionTranslator);
  }

}
