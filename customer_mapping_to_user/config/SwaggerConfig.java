package com.tnqsoftware.customer_mapping_to_user.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	private static final String TITLE = "Customer Mapping Application";
	private static final String DESCRIPTION = "This application is built for mapping the customer with the user and where it a role based application";
	private static final String VERSION = "1.0";
	private static final String TERMS_OF_SERVICE_URLS = "Terms of Service";
	private static final Contact CONTACT = new Contact("Rajesh R", "", "rajesh.ramesh@tnqsoftware.com");
	private static final String LICENSE = "Licences of APIS";
	private static final String LICENSED_URLS = "ALL APIS";
	public static final String AUTHORIZATION_HEADERS = "Authorization";

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo())
				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
	}

	private ApiKey apiKey() { 
	    return new ApiKey("JWT", AUTHORIZATION_HEADERS, "header"); 
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

	private ApiInfo getInfo() {
		// TODO Auto-generated method stub
		return new ApiInfo(TITLE, DESCRIPTION, VERSION, TERMS_OF_SERVICE_URLS, CONTACT, LICENSE, LICENSED_URLS,
				Collections.emptyList());
	}
}
