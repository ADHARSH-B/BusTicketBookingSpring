package com.ticketbooking.main.service;

import org.springframework.security.core.userdetails.User;

import com.ticketbooking.main.models.UserModel;

public interface UserService {
	UserModel saveUser(User user);

	UserModel getUser(String userName);
	

}
