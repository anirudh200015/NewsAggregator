package com.project.News.NewsAggregator.config;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;

@Configuration
public class AutoConfig {
	
	@Autowired
	AuthJwtAuthenticationFilter authJWTAuthenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	
	// to bypass springboot security 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		
		try {
			httpSecurity.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests
					.requestMatchers(HttpMethod.POST, "/register","/updatePreferences","/signin","/verifyToken","/hello")
					.permitAll()
					.requestMatchers(HttpMethod.GET, "/fetchPreferences","/getNews")
					.permitAll()
					.anyRequest()
					.authenticated())
			.formLogin(formLogin ->formLogin.defaultSuccessUrl("/hello",true).permitAll())
			.addFilterBefore(authJWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class );
			
			return httpSecurity.build();
			
		}catch(Exception e) {
			throw new RuntimeException("Error configuring security filter chain",e);
		}
		
		
		
	}
	
}
