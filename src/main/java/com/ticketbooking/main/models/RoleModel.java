package com.ticketbooking.main.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "role")
public class RoleModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	public RoleModel() {

	}

	public RoleModel(String roleName) {
		name = roleName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getname() {
		return name;
	}

	public void setname(String roleName) {
		name = roleName;
	}

}
