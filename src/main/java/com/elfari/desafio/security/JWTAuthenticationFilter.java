package com.elfari.desafio.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public JWTAuthenticationFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		try {
			AccountCredentials credentials = new ObjectMapper().readValue(request.getInputStream(), AccountCredentials.class);
		
			List<GrantedAuthority> grantedAuths = Collections.emptyList();
			
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							credentials.getUsername(), 
							credentials.getPassword(), 
							grantedAuths
							)
					);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response,
			FilterChain filterChain,
			Authentication auth) throws IOException, ServletException {
		
		AuthorizationUtil.addAuthentication(auth.getName(), response);
		
		response.getWriter().write(response.getHeader("tokenType") + response.getHeader("accessToken"));
		response.flushBuffer();
	}

}
