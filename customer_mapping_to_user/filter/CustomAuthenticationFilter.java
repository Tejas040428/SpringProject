package com.tnqsoftware.customer_mapping_to_user.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {

	private final AuthenticationManager authenticationManager;
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stu
			String username = request.getParameter("username");
			String password = request.getParameter("password"); 
			UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(username, password);
			return authenticationManager.authenticate(authenticationToken);
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		User user =(User) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		String accesToken=JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
		 .withIssuer(request.getRequestURI().toString())
		 .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
		 .sign(algorithm); 
		String refresToken=JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
				 .withIssuer(request.getRequestURI().toString())
				 .sign(algorithm); 
//		response.setHeader("access_token",accesToken);
//		response.setHeader("refresh_token", refresToken);
		Map<String, String> tokens = new HashMap<String, String>();
		tokens.put("refreshToken", refresToken);
		tokens.put("accessToken",accesToken);
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("anjfdnn");
		super.unsuccessfulAuthentication(request, response, failed);
	}

	@Override
	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		// TODO Auto-generated method stub
		super.setAuthenticationFailureHandler(authenticationFailureHandler());
	}
    @Bean
    RestAuthenticationFailureHandler authenticationFailureHandler(){
        return new RestAuthenticationFailureHandler();
    }
}
