package com.ticketbooking.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketbooking.main.models.DriverModel;

public interface DriverRepo extends JpaRepository<DriverModel, Long>{

}
