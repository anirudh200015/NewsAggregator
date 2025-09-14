package com.project.News.NewsAggregator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.News.NewsAggregator.entity.Articles;
import com.project.News.NewsAggregator.entity.User;
import com.project.News.NewsAggregator.userService.NewsService;

import reactor.core.publisher.Mono;

@RestController
public class NewsController {
	
	@Autowired
	private NewsService newsService;
	
	@GetMapping("/getNews")
	public ResponseEntity<Mono<List<Articles>>> getUserNews( ) {
		
		// needs to get the logged in user
	//	String username="test3";
		
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(newsService.getNewsByPreference(currentUser.getUsername())) ;
	}
}
