package com.ticketbooking.main.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketbooking.main.models.BookingsModel;
import com.ticketbooking.main.models.UserModel;

@Transactional
@Repository
public interface bookingsRepo extends JpaRepository<BookingsModel, Long>{

	@Query("SELECT bookings from bookingsDetails bookings JOIN bookings.userDetails u where u.id=:UserId")
	public List<BookingsModel> findByUserId(@Param("UserId") Long u);
	
	@Query("SELECT bookings from bookingsDetails bookings JOIN bookings.userDetails u where u.userName=:userName")
	public List<BookingsModel> findByUserName(@Param("userName") String u);
	
	@Modifying
	@Query("Delete From bookingsDetails bookings where bookings.busDetails.id=:busId ")
	public void deleteBookings(@Param("busId")Long busId);
	
	@Modifying
	@Query("Delete From bookingsDetails bookings where bookings.userDetails.id=:userId ")
	public void deleteBookingsByUserId(@Param("userId")Long userId);

}
