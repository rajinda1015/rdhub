package com.rad.rdhub.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@Configuration
public class RDWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable().authorizeRequests()
			.antMatchers("/**").permitAll()
			.and().httpBasic();
	}
	
	@Bean(name = "restTemplate")
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
