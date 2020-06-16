package com.rad.rdhub.interceptor;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.util.JwtTokenUtil;
import com.rad.rdhub.validator.RDBadRequestException;

@Configuration(value = "rdHubInterceptor")
public class RDHubInterceptor implements HandlerInterceptor {
	
	@Autowired
	private RDCache rdCache;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (null == request.getParameter("username") && 
				((null == request.getHeader("token") && request.getHeader("token").isEmpty())
				|| (null == request.getHeader("rfToken") && request.getHeader("rfToken").isEmpty()))) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Invalid arguments]");
		}
		
		String token = null != request.getHeader("token") ? request.getHeader("token") : request.getHeader("rfToken");
		
		try {
			boolean result = jwtTokenUtil.validateUsernameWithToken(token, request.getParameter("username"));
			if (result) {
				return result;
			} else {
				throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Token does not match with the username]");
			}
		
		} catch (ExpiredJwtException e) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Token is expired]");
		} catch (Exception e) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Unkown error : " + e.getLocalizedMessage() + "]");
		}
	}
}
