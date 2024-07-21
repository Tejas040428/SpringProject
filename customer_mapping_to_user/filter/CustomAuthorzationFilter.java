package com.tnqsoftware.customer_mapping_to_user.filter;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorzationFilter extends OncePerRequestFilter {

	private Map<String, String> error = new HashMap<String, String>();
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		if (request.getServletPath().equals("/service/login")||request.getServletPath().equals("/service/token/refresh")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					String token = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier jwtVerifier= JWT.require(algorithm).build();
					DecodedJWT decodedJWT = jwtVerifier.verify(token);
					String username = decodedJWT.getSubject();
					String [] roles=  decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
					stream(roles).forEach(role->{
						authorities.add(new SimpleGrantedAuthority(role));
						
					});
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null,authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} 
				catch (IllegalArgumentException | JWTVerificationException e) {
					// TODO Auto-generated catch block
					log.error("Error logging in : {} ", e.getMessage());
					response.setHeader("message", e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
//					response.sendError(HttpStatus.FORBIDDEN.value());		
					error.put("message", e.getMessage());
					response.setContentType(APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), error);

				}
				catch (Exception e) {
					// TODO: handle excepion
					log.error("Error logging in : {} ", e.getMessage());
					response.setHeader("message", e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
//					response.sendError(HttpStatus.FORBIDDEN.value());		
					error.put("message", e.getMessage());
					response.setContentType(APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}

	}

}
