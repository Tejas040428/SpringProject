package com.tnqsoftware.customer_mapping_to_user.security;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.tnqsoftware.customer_mapping_to_user.filter.CustomAuthenticationFilter;
import com.tnqsoftware.customer_mapping_to_user.filter.CustomAuthorzationFilter;
import com.tnqsoftware.customer_mapping_to_user.filter.RestAccessDeniedHandler;
import com.tnqsoftware.customer_mapping_to_user.filter.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
//@EnableWebMvc

public class SecurityConfig {
	@Autowired
	private UserDetailsService detailsService;

	@Autowired
	private PasswordEncoder encoders;

	private AuthenticationManager authenticationManager;
	private final static String[] PERMIT_ALL_SWAGGERS_APIS = { "/swagger-ui/*", "/v3/api-docs", "/swagger-resources/",
			"/webjars/**", "/v2/api-docs" };

	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(detailsService);

		authenticationManager = authenticationManagerBuilder.build();

		CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager);

		authenticationFilter.setFilterProcessesUrl("/service/login");

		http.csrf().disable();

		http.cors();
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()).

				authenticationEntryPoint(authenticationEntryPoint());

		http.authorizeHttpRequests().requestMatchers(PERMIT_ALL_SWAGGERS_APIS).permitAll();
		// http.authorizeHttpRequests().antMatchers
		// (PERMIT_ALL_SWAGGERS_APIS).permitAll();

		http.authorizeHttpRequests().requestMatchers("/service/login/*", "/service/token/refresh/*").permitAll();

		http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/service/save/user").permitAll();

		http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/service/user/checkusername").permitAll();

		http.authorizeHttpRequests().requestMatchers("/admin/**").hasAnyAuthority("admin");

		http.authorizeHttpRequests().requestMatchers("/admin/*").hasAnyAuthority("admin");
		http.authorizeHttpRequests().requestMatchers("/service/*").hasAnyAuthority("admin", "user");

		http.authorizeHttpRequests().anyRequest().authenticated();

		http.authenticationManager(authenticationManager).addFilter(authenticationFilter);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(new CustomAuthorzationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean

	RestAccessDeniedHandler accessDeniedHandler() {

		return new RestAccessDeniedHandler();

	}

	@Bean

	RestAuthenticationEntryPoint authenticationEntryPoint() {

		return new RestAuthenticationEntryPoint();

	}

	@Bean

	public WebSecurityCustomizer webSecurityCustomizer() {

		return (web) -> web.ignoring().requestMatchers(PERMIT_ALL_SWAGGERS_APIS);
	}

	@Bean

	CorsConfigurationSource corsConfigurationSource() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		String[] methods = { "POST", "GET", "PUT" };

		ArrayList<String> httpmethod = new ArrayList<>(Arrays.asList(methods));

		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowedMethods(httpmethod);

		source.registerCorsConfiguration("/**", new CorsConfiguration(corsConfiguration).applyPermitDefaultValues());

		return source;

	}

}
