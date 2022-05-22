package com.ticketbooking.main.dao;

import java.util.ArrayList;

public class BookTickets {
	private int busId;
	private ArrayList<Integer> seats = new ArrayList<Integer>();
	private String userName;
	private String totalCost;

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public int getBusId() {
		return busId;
	}

	public void setBusId(int busId) {
		this.busId = busId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<Integer> getSeats() {
		return seats;
	}

	public void setSeats(ArrayList<Integer> seats) {
		this.seats = seats;
	}
}
