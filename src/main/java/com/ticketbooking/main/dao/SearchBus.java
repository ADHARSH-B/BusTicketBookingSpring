package com.ticketbooking.main.dao;

public class SearchBus {
	private String boardingPoint;
	private String destinationPoint;

	public SearchBus(String boardingPoint, String destinationPoint) {
		this.boardingPoint = boardingPoint;
		this.destinationPoint = destinationPoint;
	}

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
}
