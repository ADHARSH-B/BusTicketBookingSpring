package com.ticketbooking.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import com.ticketbooking.main.models.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Long> {
	UserModel findByuserName(String username);

	UserModel findByemail(String email);

	UserModel save(User user);

	UserModel findByresetToken(String token);

}
