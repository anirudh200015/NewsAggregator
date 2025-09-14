package com.project.News.NewsAggregator.repository;

import org.springframework.stereotype.Repository;

import com.project.News.NewsAggregator.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	 public  User findByUsername(String username); 
		// TODO Auto-generated method stub
	
	



	
}
