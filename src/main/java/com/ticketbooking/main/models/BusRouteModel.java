package com.ticketbooking.main.models;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "route")
public class BusRouteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "route_id", nullable = false)
	private Long id;

	@Column(name = "boardingpoint", nullable = false)
	private String boardingPoint;

	@Column(name = "destinationpoint", nullable = false)
	private String destinationPoint;
	
	
	@ManyToMany( fetch = FetchType.EAGER)
	@JoinTable(name = "route_station", joinColumns = @JoinColumn(name = "route_id"), inverseJoinColumns = @JoinColumn(name = "station_id"))
	Set<BusStationModel> stations = new HashSet<BusStationModel>();
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<BusStationModel> getStations() {
		return stations;
	}

	public void setStations(BusStationModel stations) {
		this.stations.add(stations);
	}

	
}
