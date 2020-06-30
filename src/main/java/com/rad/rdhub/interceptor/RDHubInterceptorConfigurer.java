package com.rad.rdhub.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RDHubInterceptorConfigurer implements WebMvcConfigurer {

	@Autowired
	private RDHubInterceptor rdHubInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
				.addInterceptor(rdHubInterceptor)
				.excludePathPatterns("/js/**", "/css/**", "/images/**", "/lib/**", "/fonts/**")
				.excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
				.excludePathPatterns("/rad/getToken", "/rad/revokeToken", "/rad/removeRFToken")
				.excludePathPatterns("/userportal/visitor/registerUser");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry
				.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
