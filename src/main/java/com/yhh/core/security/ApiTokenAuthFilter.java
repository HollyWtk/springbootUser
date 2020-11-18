package com.yhh.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class ApiTokenAuthFilter extends AbstractAuthenticationProcessingFilter {

	public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
	public static final String USERNAME = "Name";

	public ApiTokenAuthFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
        //String uri = request.getRequestURI();
		String accessToken = request.getHeader(AUTHORIZATION_HEADER_NAME);
		String username = request.getHeader(USERNAME);
		// 添加websocket接口授权认证
        String auths = request.getHeader("SEC-WEBSOCKET-PROTOCOL");
        if (StringUtils.isNotBlank(auths)) {
            String[] authArr = auths.split(",");
            if (authArr.length > 1) {
                username = authArr[0].trim();
                accessToken = authArr[1].trim();
                response.addHeader("SEC-WEBSOCKET-PROTOCOL", accessToken);
            }
        }
		if (accessToken != null && username != null) { 
			return new PreAuthenticatedAuthenticationToken(username, accessToken);
		} else {
			throw new BadCredentialsException(username + " access deney");
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();

	}

}
