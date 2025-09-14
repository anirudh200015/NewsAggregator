package com.project.News.NewsAggregator.userService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class FakeNewsService {

	@Autowired
	@Qualifier("aiwebClient")
	WebClient aiWebclient;
	
	public Mono<Map<String, Object>> checkFakeNews(String text) {
        return aiWebclient.post()
                .bodyValue(Map.of("inputs", text))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<List<Map<String, Object>>>>() {})
                .map(response -> response.get(0).get(0)); // first label result
                
    }
	

	
}
