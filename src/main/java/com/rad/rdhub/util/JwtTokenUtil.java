package com.rad.rdhub.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "jwtTokenUtil")
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${security.oauth.signing.key}")
	private String signingKey;

	public boolean validateUsernameWithToken(String token, String username) throws Exception {
		final Claims claims = Jwts.parser().setSigningKey(signingKey.getBytes()).parseClaimsJws(token).getBody();
		String tokenUsername = (String) claims.get("user_name");
		if (null != tokenUsername && tokenUsername.equals(username)) {
			return true;
		} else {
			return false;
		}
	}
}
