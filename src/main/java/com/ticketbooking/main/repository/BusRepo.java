package com.ticketbooking.main.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketbooking.main.models.BusModel;
@Repository
public interface BusRepo extends JpaRepository<BusModel, Long> {
//	@Query("SELECT bus from busdetails bus JOIN bus.routes r where r.boardingPoint=:startLocation and r.destinationPoint=:endLocation and bus.departureDate=:departureDat")
	
	@Query("SELECT bus from busdetails bus JOIN bus.routes r on  r.boardingPoint=:startLocation "
			+ "and r.destinationPoint=:endLocation and bus.departureDate=:departureDat")
	public ArrayList<BusModel> findAllBusesByBoardingPointAndDestinationPoint(@Param("startLocation") String b,
			@Param("endLocation")String d,@Param("departureDat") LocalDate dd);

}
