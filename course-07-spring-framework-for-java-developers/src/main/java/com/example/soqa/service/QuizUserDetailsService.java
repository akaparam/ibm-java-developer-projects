package com.example.soqa.service;

import com.example.soqa.model.User;
import org.springframework.security.core.userdetails.UserDetails;



public interface QuizUserDetailsService {

	void register(User user);

}