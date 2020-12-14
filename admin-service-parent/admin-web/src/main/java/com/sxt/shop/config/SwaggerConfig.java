package com.sxt.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Autowired
	private SwaggerProperty swaggerProperty;


	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select().
				apis(RequestHandlerSelectors.basePackage("com.sxt.controller")).build();
	}

	private ApiInfo getApiInfo() {
		Contact contact = new Contact(swaggerProperty.getName(), swaggerProperty.getUrl(), swaggerProperty.getEmail());
		return new ApiInfoBuilder().contact(contact).title(swaggerProperty.getTitle()).description(swaggerProperty.getDecs()).termsOfServiceUrl(swaggerProperty.getTermUrl()).build();
	}
}
