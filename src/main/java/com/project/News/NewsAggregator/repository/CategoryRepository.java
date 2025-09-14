package com.project.News.NewsAggregator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.News.NewsAggregator.entity.Category;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long> {

	List<Category> findByNameIn(List<String>names);

	Optional<Category> findByName(String categoryName);
}
