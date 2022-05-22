package com.ticketbooking.main.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ticketbooking.main.models.BusModel;
import com.ticketbooking.main.models.BusRouteModel;

public interface BusRouteRepo extends JpaRepository<BusRouteModel, Long>{
	@Query("SELECT r from route r where r.boardingPoint=:startLocation and r.destinationPoint=:endLocation")
	public ArrayList<BusRouteModel> findRoute(@Param("startLocation") String b,@Param("endLocation")String d);
	
	@Query("SELECT r from route r where r.boardingPoint=:startLocation")
	public ArrayList<BusRouteModel> findBoardingLocations(@Param("startLocation") String b);
	
	@Query("SELECT r from route r where r.boardingPoint=:endLocation")
	public ArrayList<BusRouteModel> findDestinationLocations(@Param("endLocation") String b);
	
}
