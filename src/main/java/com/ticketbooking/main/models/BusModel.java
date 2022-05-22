package com.ticketbooking.main.models;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "busdetails")
public class BusModel {
	public BusModel() {

	}

	public BusModel(String busName, boolean isAvailable, String currentLocation) {
		super();
		this.busName = busName;
		this.isAvailable = isAvailable;
		this.currentLocation = currentLocation;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bus_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String busName;

	@Column(name = "isAvailable", nullable = false)
	private boolean isAvailable;

	@Column(name = "currentLocation", nullable = false)
	private String currentLocation;

	@Column(name = "totalSeats", nullable = false)
	private int totalSeats;

	@Column(name = "price", nullable = false)
	private String price;

	@Column(name = "seatsBooked", nullable = false)
	private int seatsBooked;

	@Column(name = "seatsAvailable", nullable = false)
	private int seatsAvailable;

	@Column(name = "busType", nullable = false)
	private String busType;

	@Column(name = "departureTime", nullable = false)
	private LocalTime departureTime = LocalTime.now();

	@Column(name = "arrivalTime", nullable = false)
	private LocalTime arrivalTime = LocalTime.now();

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "bus", cascade = CascadeType.ALL)
	Set<BusSeatsModel> busSeats = new HashSet<BusSeatsModel>();

	@Column(name = "departureDate", nullable = false)
	private LocalDate departureDate = LocalDate.now();

	@Column(name = "arrivalDate", nullable = false)
	private LocalDate arrivalDate = LocalDate.now();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "bus_route", joinColumns = @JoinColumn(name = "bus_Id"), inverseJoinColumns = @JoinColumn(name = "route_Id"))
	private List<BusRouteModel> routes = new ArrayList<BusRouteModel>();

	@PreRemove
	public void removeRoute() {
		routes.clear();
		busSeats.clear();
	}
	


	public String getPrice() {
		return price;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setSeatsBooked(int seatsBooked) {
		this.seatsBooked = seatsBooked;
	}

	public void setSeatsAvailable(int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}

	public Set<BusStationModel> getStations() {
		return this.routes.get(0).getStations();
	}

	public String getBusType() {
		return busType;
	}

	public Set<BusSeatsModel> getBusSeats() {
		return busSeats;
	}

	public void addBusSeat(BusSeatsModel busSeat) {
		this.busSeats.add(busSeat);
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getBoardingPoint() {
		return this.routes.get(0).getBoardingPoint();
	}

	public String getdestinationPoint() {
		return this.routes.get(0).getDestinationPoint();
	}

	public BusRouteModel getRoutes() {
		return routes.get(0);
	}

	public void setRoute(ArrayList<BusRouteModel> route) {
		this.routes = route;
	}

	public int seatCount() {
		return totalSeats;
	}

	public void addSeatCount(int seatCount) {
		this.totalSeats += seatCount;
	}

	public int getSeatsBooked() {
		return seatsBooked;
	}

	public void incSeatsBooked(int seatsBooked) {
		this.seatsBooked += seatsBooked;
	}

	public int getSeatsAvailable() {
		return seatsAvailable;
	}

	public void incAvailableSeats(int seatsCount) {
		this.seatsAvailable += seatsCount;
	}

	public void decAvailableSeats(int seatsCount) {
		this.seatsAvailable -= seatsCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

}
