package com.ticketbooking.main.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "busSeats")
public class BusSeatsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "seat_id", nullable = false)
	private Long id;

	@Column(name = "seat_type", nullable = false)
	private String seatType;

	@Column(name = "isSeatBooked", nullable = false)
	private boolean isBooked;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "busId")
	BusModel bus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	UserModel user;

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public UserModel getUser() {
		return user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public void setBus(BusModel bus) {
		this.bus = bus;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

}
