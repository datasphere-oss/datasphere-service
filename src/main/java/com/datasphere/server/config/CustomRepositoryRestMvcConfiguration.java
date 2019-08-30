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

package com.datasphere.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.datasphere.server.domain.workspace.Workspace;
import com.datasphere.server.domain.workspace.WorkspacePagedResourcesAssembler;

/**
 * Custom Configuration
 * - https://github.com/spring-projects/spring-boot/issues/6529
 */
@Configuration
public class CustomRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

	
public CustomRepositoryRestMvcConfiguration(ApplicationContext context,
			ObjectFactory<ConversionService> conversionService) {
		super(context, conversionService);
		// TODO Auto-generated constructor stub
	}

/**
   * ObjectMapper definition used in Spring Data Rest (use @Primary to prevent other Framework conflict)
   *
   * When processing json + hal type information outside the Spring Data Rest management,
   * In the HypermediaSupportBeanDefinitionRegistrar we add a TypeConstrainedMappingJackson2HttpMessageConverter
   * At this time, it loads the ObjectMapper with the name _halObjectMapper and adds name handling for the same ObjectMapper sphere of influence.
   * @return
   */
  @Override
  @Bean(name = {"objectMapper", "_halObjectMapper"})
  @Primary
  public ObjectMapper objectMapper() {
    return super.objectMapper();
  }

  @Bean
  public WorkspacePagedResourcesAssembler<Workspace> workspacePagedResourcesAssembler() {
    return new WorkspacePagedResourcesAssembler<>(super.pageableResolver(), null);
  }

}
