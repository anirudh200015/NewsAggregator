package com.project.News.NewsAggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveConfig {

	@Value("${huggingface.token}")
	private  String HFToken;
	
	@Bean
	public WebClient webclient () {
		return WebClient.builder()
				.baseUrl("https://newsapi.org/v2/top-headlines")
				.build();
		
	}
	
	
	@Bean
	public WebClient aiwebClient() {
		
		
		return WebClient.builder()
				.baseUrl("https://api-inference.huggingface.co/models/mrm8488/bert-tiny-finetuned-fake-news-detection")
                .defaultHeader("Authorization", "Bearer "+HFToken)
                .build();
				
	}
	
}
