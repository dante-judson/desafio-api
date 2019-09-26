package com.judson.desafio.api.interceptor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.judson.desafio.api.interceptor.CustomSecurityInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer  {

	@Bean
	public CustomSecurityInterceptor customSecurityInterceptor() {
		return new CustomSecurityInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(customSecurityInterceptor());
	}

}
