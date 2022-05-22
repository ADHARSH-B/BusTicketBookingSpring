package com.ticketbooking.main.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthResponse {
	private String message;
	private String AuthToken;
	private String refreshToken;
	private String username;
	private ArrayList<? extends GrantedAuthority> role = new ArrayList<>();

	public String getMessage() {
		return message;
	}

	public ArrayList<? extends GrantedAuthority> getRole() {
		return role;
	}

	public void setRole(Collection<? extends GrantedAuthority> collection) {
		this.role = (ArrayList<? extends GrantedAuthority>) collection;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthToken() {
		return AuthToken;
	}

	public void setAuthToken(String authToken) {
		AuthToken = authToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
