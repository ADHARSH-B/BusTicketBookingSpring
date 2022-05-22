package com.ticketbooking.main.service;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ticketbooking.main.models.RoleModel;
import com.ticketbooking.main.models.UserModel;
import com.ticketbooking.main.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userrepo;

	
	@Override
	public UserServiceImpl loadUserByUsername(String username) throws UsernameNotFoundException {

		UserModel usermodel = userrepo.findByuserName(username);
	
		if (usermodel == null) {
			throw new UsernameNotFoundException("User not exists");
		}
		return new UserServiceImpl(usermodel);
	}
}
