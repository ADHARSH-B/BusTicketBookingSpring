package com.ticketbooking.main.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketbooking.main.models.BusSeatsModel;

@Transactional
@Repository
public interface BusSeatsRepo extends JpaRepository<BusSeatsModel, Long>{
	@Modifying
	@Query("Delete From busSeats seats where seats.bus.id=:busId ")
	public void deleteSeats(@Param("busId")Long busId);
	
	@Modifying
	@Query("Update From busSeats seats SET isBooked=0,user=NULL where seats.user.id=:userId")
	public void updateSeats(@Param("userId") Long userId);
}
