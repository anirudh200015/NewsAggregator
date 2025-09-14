package com.project.News.NewsAggregator.userService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.News.NewsAggregator.entity.Articles;
import com.project.News.NewsAggregator.entity.Category;
import com.project.News.NewsAggregator.entity.NewsResponse;
import com.project.News.NewsAggregator.entity.User;
import com.project.News.NewsAggregator.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NewsService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	WebClient webclient;
	
	@Autowired
	FakeNewsService fakeNews;
	
	@Value("${API_KEY}")
	private  String API_KEY;
	
	
	public Mono<List<Articles>> getNewsByPreference(String username) {
		// TODO Auto-generated method stub
		
		
		User user= userRepository.findByUsername(username);
		
		if(user==null) {
			throw new RuntimeException("User Not found");
		}
		
		List<Category> userPreferences= user.getPreferences();
		
		// Fetching the news from the API 
		
		
		
		if(userPreferences.isEmpty()) {
			return null;
		}
		
//		return Flux.fromIterable(userPreferences)                          // loop reactively
//                .flatMap(category -> getNewsByCategory(category.getName())) // Mono<List<Article>> for each category
//                .flatMapIterable(articles -> articles)              // flatten List<Article> -> Flux<Article>
//                .collectList();                                     
//		
		
		return Flux.fromIterable(userPreferences)                          
		        .flatMap(category -> getNewsByCategory(category.getName())) // Mono<List<Article>>
		        .flatMapIterable(articles -> articles)                      // Flux<Article>
		        .flatMap(article -> 
		        fakeNews.checkFakeNews(article.getTitle() + " " + article.getDescription())
		                .map(result -> {
		                    String label = (String) result.get("label");
		                    Double score = (Double) result.get("score");
		                    
		                    String credibility =null;
		                    if(label.equals("LABEL_0")) {
		                    	credibility="Fake news";
		                    }
		                    	
		                    if(label.equals("LABEL_1")) {
		                    	credibility="Credible news";
		                    }
		                    
		                    
		                    	
		                    article.setCredibility(credibility);
		                    article.setConfidence(score);
		                    return article;  // return enriched article
		                })
		        )
		        .collectList();           		


	}



	private Mono<List<Articles>> getNewsByCategory(String category) {
		// TODO Auto-generated method stub
		
		
			return webclient.get()
			        .uri(uriBuilder -> uriBuilder
			                .queryParam("q", category)
			                .queryParam("apiKey",API_KEY )
			                .build())
			        .retrieve()
			        .bodyToMono(NewsResponse.class)
			        .map(NewsResponse::getArticles);
		
    
		
	
	}

	
}
