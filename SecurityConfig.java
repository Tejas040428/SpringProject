package com.tnqsoftware.customer_mapping_to_user.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService detailsService;
	@Autowired
	private PasswordEncoder encoder;

	private final static String[] PERMIT_ALL_SWAGGERS_APIS = { "/swagger-ui/**", "/v3/api-docs",
			"/swagger-resources/**", "/webjars/**", "/v2/api-docs" };

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(detailsService).passwordEncoder(encoder);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(PERMIT_ALL_SWAGGERS_APIS);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
		authenticationFilter.setFilterProcessesUrl("/service/login");
		http.csrf().disable();
		http.cors();
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint());

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers(PERMIT_ALL_SWAGGERS_APIS).permitAll();
		http.authorizeRequests().antMatchers("/service/login/**", "/service/token/refresh/**").permitAll();
		http.authorizeRequests().antMatchers(POST, "/service/save/user").permitAll();
		http.authorizeRequests().antMatchers(POST, "/service/user/checkusername").permitAll();
		http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("admin");
		http.authorizeRequests().antMatchers(PUT, "/admin/**").hasAnyAuthority("admin");
		http.authorizeRequests().antMatchers(GET, "/service/**").hasAnyAuthority("admin", "user");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(authenticationFilter);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(new CustomAuthorzationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
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

	@Bean
	RestAccessDeniedHandler accessDeniedHandler() {
		return new RestAccessDeniedHandler();
	}

	@Bean
	RestAuthenticationEntryPoint authenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}
}
