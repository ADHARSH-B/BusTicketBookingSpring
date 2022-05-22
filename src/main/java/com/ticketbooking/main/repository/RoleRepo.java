package com.ticketbooking.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ticketbooking.main.models.RoleModel;

@Repository
public interface RoleRepo extends JpaRepository<RoleModel, Long> {
	
     RoleModel findByname(String RoleName);

}

