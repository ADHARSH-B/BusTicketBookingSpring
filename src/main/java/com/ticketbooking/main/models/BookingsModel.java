package com.ticketbooking.main.models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "bookingsDetails")
public class BookingsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bookingId", nullable = false)
	private Long id;

	@Column(name = "totalCost", nullable = false)
	private int totalCost;

	@Column(name = "bookingDate", nullable = false)
	private LocalDateTime bookingDate = LocalDateTime.now();

	@Column(name = "bookingStatus", nullable = false)
	private boolean isCancelled = false;

	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	private Set<BusSeatsModel> bookedSeats = new HashSet<BusSeatsModel>();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	UserModel userDetails;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "busId")
	BusModel busDetails;
	
	@PreRemove
	public void busRemove() {
		this.busDetails=null;
		bookedSeats.clear();
	}
	
	

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Set<BusSeatsModel> getBookedSeats() {
		return bookedSeats;
	}

	public void addBookedSeats(BusSeatsModel bookedSeat) {
		this.bookedSeats.add(bookedSeat);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	@JsonIgnore
	public UserModel getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserModel userDetails) {
		this.userDetails = userDetails;
	}

	public BusModel getBusDetails() {
		return busDetails;
	}

	public void setBusDetails(BusModel busDetails) {
		this.busDetails = busDetails;
	}

//	@JsonIgnore
	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}

}
