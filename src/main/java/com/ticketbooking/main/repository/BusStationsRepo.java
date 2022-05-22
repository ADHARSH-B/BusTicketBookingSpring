package com.ticketbooking.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketbooking.main.models.BusStationModel;

public interface BusStationsRepo extends JpaRepository<BusStationModel, Long>{

}
