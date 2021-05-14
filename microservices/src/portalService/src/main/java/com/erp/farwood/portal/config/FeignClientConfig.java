package com.erp.farwood.portal.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@EnableAutoConfiguration
@Configuration
public class FeignClientConfig {

	@Bean
    public Logger.Level feignLogger () {
      return Logger.Level.FULL;
    }
} 

