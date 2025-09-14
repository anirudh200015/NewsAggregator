package com.project.News.NewsAggregator.userService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.News.NewsAggregator.entity.Category;
import com.project.News.NewsAggregator.entity.User;
import com.project.News.NewsAggregator.entity.UserDTO;
import com.project.News.NewsAggregator.entity.VerificationToken;
import com.project.News.NewsAggregator.repository.CategoryRepository;
import com.project.News.NewsAggregator.repository.UserRepository;
import com.project.News.NewsAggregator.repository.VerificationTokenRepository;
import com.project.News.NewsAggregator.util.TokenUtil;





@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	public User registerUser(UserDTO userDto) {
		
		User user= new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		user.setEnabled(false);
		user.setRole("ADMIN");
		
		return userRepository.save(user);
		
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User fetchedUser = userRepository.findByUsername(username);
		
		if(fetchedUser==null) {
			throw new UsernameNotFoundException("username not found");
		}
		
		return org.springframework.security.core.userdetails.User
			.withUsername(fetchedUser.getUsername())
			.password(fetchedUser.getPassword())
			.roles(fetchedUser.getRole())
			.disabled(false)
			.build();
			
				
		
	}


	public String loginUser(String username, String password) {
		// TODO Auto-generated method stub
		User user= userRepository.findByUsername(username);
		if(user==null) {
			return " User not found ";
		}
		
		if(!user.isEnabled()) {
			return "user is not enabled, verify your account." ;
		}
		
		String fetchedPassword= user.getPassword();
		boolean isMatch= passwordEncoder.matches(password, fetchedPassword);
		
		if (!isMatch) {
			return "invalid password"; 
		}
		
		return TokenUtil.generateToken(user, user.getRole());
		
				
	}


	public void updatePreferences(String username,List<String>categories) {
		// TODO Auto-generated method stub
		
		User user= userRepository.findByUsername(username);
		if(user==null) {
			throw new RuntimeException("User Not found");
		}
		
		
//		List<Category>categoryEntities= categoryRepository.findByNameIn(categories);
//		
//		if (categoryEntities.isEmpty()) {
//	        throw new RuntimeException("No matching categories found: " + categories);
//	    }
		
		List<Category>categoriesEntities= new ArrayList<>();
		

	    for (String categoryName : categories) {
	        Category category = categoryRepository.findByName(categoryName)
	                .orElseGet(() -> {
	                    // create new if it doesn’t exist
	                    Category newCategory = new Category();
	                    newCategory.setName(categoryName);
	                    return categoryRepository.save(newCategory);
	                });
	        
	    
	        categoriesEntities.add(category);
	    }
	    
	    user.getPreferences().addAll(categoriesEntities);
	    //user.setPreferences(categoriesEntities);

		userRepository.save(user);	
	}


	public List<Category> getPreferences(String username) {
		// TODO Auto-generated method stub
		User user= userRepository.findByUsername(username);
		if(user==null) {
			throw new RuntimeException("User Not found");
		}
		
		
		List<Category>UserPreferences=user.getPreferences();
		return UserPreferences;
	}


	public void saveVerificationToken(User newUser, String token) {
		// TODO Auto-generated method stub
		
		VerificationToken verificationToken= new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(newUser);
		verificationToken.setExpireDate(new Date(System.currentTimeMillis()+1000*60*60*24));
		verificationTokenRepository.save(verificationToken);
	}


	

	public Boolean verifyRegistrationToken(String token) {
		// TODO Auto-generated method stub
		VerificationToken registeredToken= verificationTokenRepository.findByToken(token);
		
		if(token==null) {
			return false;
		}
		Long registeredExpireTime=registeredToken.getExpireDate().getTime();
		
		if(registeredExpireTime < System.currentTimeMillis()) {
			return false;
		}
		
		return true;
	}


	public void enabaleUser(String token) {
		// TODO Auto-generated method stub
		
		VerificationToken verificationToken= verificationTokenRepository.findByToken(token);
		
		User fetchedUser = verificationToken.getUser();
		
		fetchedUser.setEnabled(true);
		userRepository.save(fetchedUser);
		verificationTokenRepository.delete(verificationToken);
	}
	
}
