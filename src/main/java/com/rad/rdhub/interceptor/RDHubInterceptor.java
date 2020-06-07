package com.rad.rdhub.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.util.JwtTokenUtil;
import com.rad.rdhub.validator.RDBadRequestException;

@Configuration(value = "rdHubInterceptor")
public class RDHubInterceptor implements HandlerInterceptor {
	
	@Autowired
	private RDCache rdCache;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (null == request.getParameter("username") || null == request.getHeader("token") || request.getHeader("token").isEmpty()) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Invalid arguments]");
		}
		
		boolean result = jwtTokenUtil.validateUsernameWithToken(request.getHeader("token"), request.getParameter("username"));
		if (result) {
			return result;
		} else {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Token does not match with the username]");
		}
	}
}
