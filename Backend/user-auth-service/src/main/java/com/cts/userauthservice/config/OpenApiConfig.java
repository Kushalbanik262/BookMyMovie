package com.cts.userauthservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	@Bean
	OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
			@Value("${application-version}") String appVersion) {
		return new OpenAPI().info(new Info().title("User Authentication Service").version(appVersion)
				.description(appDesciption).contact(new Contact().name("Kushal Banik").email("kushalbanik93@gmail.com"))
				.license(new License().name("MIT")));
	}
}
