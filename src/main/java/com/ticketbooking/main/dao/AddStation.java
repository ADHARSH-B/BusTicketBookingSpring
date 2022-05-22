package com.ticketbooking.main.dao;

import java.util.ArrayList;
import java.util.List;

public class AddStation {
	private String boardingPoint;
	private String destinationPoint;
	List<String> stations = new ArrayList<String>();

	public String getBoardingPoint() {
		return boardingPoint;
	}

	public void setBoardingPoint(String boardingPoint) {
		this.boardingPoint = boardingPoint;
	}

	public String getDestinationPoint() {
		return destinationPoint;
	}

	public void setDestinationPoint(String destinationPoint) {
		this.destinationPoint = destinationPoint;
	}

	public List<String> getStations() {
		return stations;
	}

	public void setStations(List<String> stations) {
		this.stations = stations;
	}

}
