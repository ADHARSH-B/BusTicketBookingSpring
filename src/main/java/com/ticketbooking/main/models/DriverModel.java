package com.ticketbooking.main.models;

//import java.util.HashSet;
//import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity(name = "driverdetails")
public class DriverModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "driver_id", nullable = false)
	private Long id;
	
	@Column(name = "name", nullable = false)
	public String driverName;
	
	@Column(name = "contactnumber", nullable = false,unique=true)
	public String contactnumber;
	
	@Column(name = "aadharnumber", nullable = false,unique=true )
	public String aadharnumber;
	
	@Column(name="age",nullable = false)
	public int age;
	

	public DriverModel() {
	}
	
	
	public DriverModel(String driverName, String contactnumber, String aadharnumber, int age) {
		this.driverName = driverName;
		this.contactnumber = contactnumber;
		this.aadharnumber = aadharnumber;
		this.age = age;
//		this.bus = bus;
	}


	public String getDriverName() {
		return driverName;
	}


	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}


	public String getContactnumber() {
		return contactnumber;
	}


	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}


	public String getAadharnumber() {
		return aadharnumber;
	}


	public void setAadharnumber(String aadharnumber) {
		this.aadharnumber = aadharnumber;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}





}
