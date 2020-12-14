package com.sxt.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "swagger2")
@Data
public class SwaggerProperty {
	
  private String name;
  private String email;
  private String  url;
  private String  title;
  private String decs;
  private String  termUrl;
}
