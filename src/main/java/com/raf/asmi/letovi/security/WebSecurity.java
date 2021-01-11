package com.raf.asmi.letovi.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private CustomAuthenticationProvider customAuthenticationProvider;
	private BCryptPasswordEncoder encoder;

	
	public WebSecurity(CustomAuthenticationProvider customAuthenticationProvider){
		super();
		this.customAuthenticationProvider=customAuthenticationProvider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*http.cors().and().csrf().disable().authorizeRequests().antMatchers(SecurityConstants.LETOVI_PATH, SecurityConstants.LOGIN_PATH).permitAll()
			.anyRequest().authenticated().and().addFilter(new JWTAuthorizationFilter(authenticationManager())).sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(SecurityConstants.LETOVI_PATH, SecurityConstants.AVIONI_PATH).permitAll()
		.anyRequest().authenticated().and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager())).sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		/*
		http
		.cors()
		.and()
		.csrf().disable()
        .authorizeRequests()
        .anyRequest().permitAll();*/
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.addExposedHeader("Authorization");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		// customAuthenticationProvider.setEncoder(encoder);
		auth.authenticationProvider(customAuthenticationProvider);
	}
}
