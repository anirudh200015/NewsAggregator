package com.project.News.NewsAggregator.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.News.NewsAggregator.entity.User;
import com.project.News.NewsAggregator.entity.UserDTO;
import com.project.News.NewsAggregator.userService.UserService;



@RestController
public class UserController {
	
	
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public User registerUser(@RequestBody UserDTO user) {
		
		User NewUser= userService.registerUser(user);
		
		String verificationToken=UUID.randomUUID().toString();
		String verificationUrl="http://localhost:8080/verifyToken?token="+verificationToken;
		System.out.println("Please verify your registration by clicking on the following link: "+verificationUrl);
		
		userService.saveVerificationToken(NewUser,verificationToken);
		
		return NewUser;
		
	}
	
	@PostMapping("/verifyToken")
	public String verifyRegistrationToken(@RequestParam String token) {
		
		Boolean isVerificationSuccessfull= userService.verifyRegistrationToken(token);
		
		if(isVerificationSuccessfull) {
			userService.enabaleUser(token);
			return "Account is verified successfully , Please proceed to login";
		}
		
		return "Token validation failed, Try again.";
		
	}
	
	
	@GetMapping("/hello")
	public String hello() { 
		
		return "Hello world";
	}
	
	@PostMapping("/signin")
	public String loginUser(@RequestParam String username, @RequestParam String password) {
		return userService.loginUser(username, password);
	}
	
}
