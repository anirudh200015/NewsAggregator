
package com.project.News.NewsAggregator.entity;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long  userId;
	
	private String username;
	private String password;
	
	private String role;
	private  boolean isEnabled;
	
	@ManyToMany
	private List<Category> preferences;
	
	
	public List<Category> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Category> preferences) {
		this.preferences = preferences;
	}

	public User() {}
	
	public User(long userId, String username, String password, String role, boolean isEnabled) {
		
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.role = role;
		this.isEnabled = isEnabled;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	
	
	
}
