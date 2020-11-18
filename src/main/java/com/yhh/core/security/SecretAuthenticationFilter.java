package com.yhh.core.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class SecretAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public SecretAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {


		return new PreAuthenticatedAuthenticationToken("", "");

	}

}
