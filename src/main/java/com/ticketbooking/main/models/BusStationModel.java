package com.ticketbooking.main.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name = "busstations")
public class BusStationModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "station_id", nullable = false)
	private Long id;

	@Column(name = "stationname", nullable = false)
	private String stationname;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "stations")
	private Set<BusRouteModel> busroutes = new HashSet<BusRouteModel>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStationname() {
		return stationname;
	}

	public void setStationname(String stationname) {
		this.stationname = stationname;
	}

}
