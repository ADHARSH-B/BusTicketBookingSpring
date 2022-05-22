package com.ticketbooking.main.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ticketbooking.main.models.RoleModel;
import com.ticketbooking.main.models.UserModel;
import com.ticketbooking.main.repository.UserRepo;


@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetails {
	@Autowired
	private UserRepo userrepo;
	
	private UserModel user;
	
	public UserServiceImpl() {
		
	}
	public UserServiceImpl(UserModel user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<RoleModel> rolesSet =user.getRoles();
		ArrayList<GrantedAuthority> roles=new ArrayList<GrantedAuthority>();
	
		for(RoleModel role: rolesSet)
			roles.add(new SimpleGrantedAuthority(role.getname()));
		return roles;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}
	

//	public String getEmail() {
//		return user.getEmail();
//	}
	
	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public UserModel saveUser(User user) {
		return userrepo.save(user);
	}

	@Override
	public UserModel getUser(String userName) {
		return userrepo.findByuserName(userName);
	}

}
