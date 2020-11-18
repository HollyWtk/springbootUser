package com.yhh.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerifyCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public VerifyCodeAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		return new PreAuthenticatedAuthenticationToken("", "");

	}

}
