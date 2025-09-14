package com.project.News.NewsAggregator.util;

import java.util.Date;

import com.project.News.NewsAggregator.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public class TokenUtil {

	public static String generateToken(User user, String role) {
		// TODO Auto-generated method stub
		 
		return Jwts.builder().subject(user.getUsername()).setIssuedAt(new Date()).setExpiration(
				new Date(System.currentTimeMillis()+8*60*1000))
				.claim("roles","ROLE_"+user.getRole())
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256,"secretKeyforNewsAggregatorprojectbymeandItisTemporarykey")
				.compact();
	}

	public static boolean validateSignedToken(String authorizationHeader) {
		// TODO Auto-generated method stub
		Claims body= Jwts.parser()
				.setSigningKey("secretKeyforNewsAggregatorprojectbymeandItisTemporarykey")
				.build()
				.parseClaimsJws(authorizationHeader)
				.getBody();
		return true;
	}

	public static String getUsername(String authorizationHeader) {
		// TODO Auto-generated method stub
		String username= Jwts.parser()
				.setSigningKey("secretKeyforNewsAggregatorprojectbymeandItisTemporarykey")
				.build()
				.parseClaimsJws(authorizationHeader)
				.getBody()
				.getSubject();
		
		System.out.println("username is : " + username);
		return username;
	}

	
	
}
