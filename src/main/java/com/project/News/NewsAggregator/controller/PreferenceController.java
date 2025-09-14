package com.project.News.NewsAggregator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.News.NewsAggregator.entity.Category;
import com.project.News.NewsAggregator.entity.User;
import com.project.News.NewsAggregator.userService.UserService;

@RestController
public class PreferenceController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/updatePreferences")
	public String savePreferences(@RequestBody List<String>categories) {
		
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.updatePreferences(currentUser.getUsername(), categories);
		
		return "Preferences are updated successfully!!";
		
	}
	
	
	@GetMapping("/fetchPreferences")
	public List<Category> getPreferences(){
		
	//	String username="test3";
		
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		

		List<Category> userPreferences=userService.getPreferences(currentUser.getUsername());
		return userPreferences;
	}
	
}
