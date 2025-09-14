package com.project.News.NewsAggregator.config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import com.project.News.NewsAggregator.entity.User;
import com.project.News.NewsAggregator.repository.UserRepository;
import com.project.News.NewsAggregator.util.TokenUtil;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class AuthJwtAuthenticationFilter extends OncePerRequestFilter{
	
	
	
	@Autowired
	UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String authorizationHeader= request.getHeader("authorization");
		
		if(authorizationHeader !=null  && authorizationHeader.startsWith("Bearer ")) {
			authorizationHeader=authorizationHeader.substring(7);
		}
		
		if(authorizationHeader==null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Missing Auth header");
			return;
		}
		
		if(!TokenUtil.validateSignedToken(authorizationHeader)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid user token");
			return;
		}
		
		
		
		String username= TokenUtil.getUsername(authorizationHeader);
		User user= userRepository.findByUsername(username);
		
		
		UsernamePasswordAuthenticationToken authentication =
		        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		
		// move to next level (either controller or filter 2)
		filterChain.doFilter(request, response);
		
	}
		
		@Override
		protected boolean shouldNotFilter(HttpServletRequest request)  {
			String path= request.getRequestURI();
			return path.contains("register")|| path.contains("signin")||path.contains("verifyToken");
		} 
	
	
}
