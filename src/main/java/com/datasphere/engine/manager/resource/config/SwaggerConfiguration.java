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

package com.datasphere.engine.manager.resource.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Value("${security.oauth2.client.client-id}")
	private String CLIENT_ID;

	@Value("${security.oauth2.client.client-secret}")
	private String CLIENT_SECRET;

	@Value("${security.oauth2.client.access-token-uri}")
	private String TOKEN_URL;

	@Value("${security.oauth2.client.user-authorization-uri}")
	private String AUTH_URL;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(metadata())
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()));
	}

	private ApiInfo metadata() {
		return new ApiInfoBuilder()
				.title("ResourceManager REST API")
				.description("REST API for resource manager")
				.version("1.0.0")
				.license("")
				.licenseUrl("")
				.build();
	}

	@Bean
	public SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder()
				.clientId(CLIENT_ID)
				.clientSecret(CLIENT_SECRET)
				.scopeSeparator(" ")
				.useBasicAuthenticationWithAccessCodeGrant(true)
				.build();
	}

	private SecurityScheme securityScheme() {
		GrantType grantType = new AuthorizationCodeGrantBuilder()
				.tokenEndpoint(new TokenEndpoint(TOKEN_URL, "oauthtoken"))
				.tokenRequestEndpoint(new TokenRequestEndpoint(AUTH_URL, CLIENT_ID, CLIENT_SECRET))
				.build();

		SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
				.grantTypes(Arrays.asList(grantType))
				.scopes(Arrays.asList(scopes()))
				.build();
		return oauth;
	}

	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scopes = {
				new AuthorizationScope("read", "read"),
				new AuthorizationScope("write", "write") };
		return scopes;
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(new SecurityReference("spring_oauth", scopes())))
				.forPaths(PathSelectors.any())
				.build();
	}
}
