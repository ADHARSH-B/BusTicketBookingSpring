package com.ticketbooking.main.controller;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ticketbooking.main.dao.ErrorMessage;
import com.ticketbooking.main.dao.SuccessMessage;
import com.ticketbooking.main.dao.AddStation;
import com.ticketbooking.main.models.BookingsModel;
import com.ticketbooking.main.models.BusModel;
import com.ticketbooking.main.models.BusRouteModel;
import com.ticketbooking.main.models.BusSeatsModel;
import com.ticketbooking.main.models.BusStationModel;
import com.ticketbooking.main.models.RoleModel;
import com.ticketbooking.main.models.UserModel;
import com.ticketbooking.main.repository.BusRepo;
import com.ticketbooking.main.repository.BusRouteRepo;
import com.ticketbooking.main.repository.BusSeatsRepo;
import com.ticketbooking.main.repository.BusStationsRepo;
import com.ticketbooking.main.repository.RoleRepo;
import com.ticketbooking.main.repository.UserRepo;
import com.ticketbooking.main.repository.bookingsRepo;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	@Autowired
	private UserRepo userrepo;

	@Autowired
	private RoleRepo rolerepo;

	@Autowired
	BusRouteRepo busRouteRepo;

	@Autowired
	bookingsRepo bookingsrepo;
	
	@Autowired
	private BusStationsRepo busStationRepo;
	
	@Autowired
	BusSeatsRepo busseatsrepo;

	@Autowired
	private BusRepo busrepo;

	@Autowired
	private BusSeatsRepo busSeatsRepo;

	@DeleteMapping("/deleteuser/{username}")
	public ResponseEntity<?> deleteUser(@PathVariable String username) {
		UserModel user = userrepo.findByuserName(username);
		if (user != null) {
			userrepo.delete(user);
			return new ResponseEntity<>(new ErrorMessage("userdeletion Success ", HttpStatus.ACCEPTED),
					HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new ErrorMessage("userdeletion error ", HttpStatus.ACCEPTED), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUseDetails(@PathVariable("id") String Id) {
	
		bookingsrepo.findByUserId(Long.parseLong(Id)).forEach(bookings->{
			BusModel bus=  busrepo.findById(bookings.getBusDetails().getId()).get();
			bus.setSeatsAvailable(bus.getSeatsAvailable()+ bookings.getBookedSeats().size());
			bus.setSeatsBooked(Math.abs(bus.getSeatsBooked()-bookings.getBookedSeats().size()));
			busrepo.save(bus);
		});
		busseatsrepo.updateSeats(Long.parseLong(Id));
		bookingsrepo.deleteBookingsByUserId(Long.parseLong(Id));
		userrepo.deleteById(Long.parseLong(Id));
	
		return new ResponseEntity<>(new SuccessMessage("User Deletion Success", HttpStatus.OK), HttpStatus.OK);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUserDetails(@PathVariable("id") String Id) {

		Optional<UserModel> user = userrepo.findById(Long.parseLong(Id));
		return ResponseEntity.ok(user.get());
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUserDetails(@PathVariable("id") String Id, @RequestBody Map<String, String> User) {
		Optional<UserModel> user = userrepo.findById(Long.parseLong(Id));

		Set<RoleModel> r = new HashSet<RoleModel>();
		r.add(rolerepo.findByname(User.get("role")));
		UserModel u = user.get();
		u.setEmail(User.get("email"));
		u.setName(User.get("name"));
		u.setUserName(User.get("UserName"));
		u.setRoles(r);
		userrepo.save(u);
		return new ResponseEntity<>(new SuccessMessage("User Updation Success", HttpStatus.CREATED),
				HttpStatus.CREATED);

	}

	@GetMapping("/allUsers")
	public ResponseEntity<?> getAllUsers() {
		List<UserModel> user = userrepo.findAll();

		if (user == null)
			return new ResponseEntity<>(new ErrorMessage("No users available", HttpStatus.UNAUTHORIZED),
					HttpStatus.UNAUTHORIZED);

		return ResponseEntity.ok(user);
	}

	@PutMapping("/updatebus/{id}")
	public ResponseEntity<?> updateBusDetails(@PathVariable("id") String Id,
			@RequestBody Map<String, String> busDetails) {
		System.out.println(busDetails);
		BusModel bus = busrepo.findById((long) Long.parseLong(Id)).get();
		bus.setArrivalDate(LocalDate.parse(busDetails.get("ArrivalDate")));
		bus.setDepartureDate(LocalDate.parse(busDetails.get("DepartureDate")));
		ArrayList<BusRouteModel> busroute = busRouteRepo.findRoute(busDetails.get("BoardingPoint"),
				busDetails.get("DestinationPoint"));
		if (busroute.size() == 0) {
			return new ResponseEntity<>(new ErrorMessage("Route Not Found", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

		Set<BusSeatsModel> seats = bus.getBusSeats();
		seats.forEach(c -> {
			c.setSeatType(busDetails.get("SeatType"));
		});

		for (int i = 0; i < Integer.parseInt(busDetails.get("AddSeats")); i++) {
			BusSeatsModel seat = new BusSeatsModel();
			seat.setBus(bus);
			seat.setSeatType(busDetails.get("SeatType"));
			busSeatsRepo.save(seat);
			bus.addBusSeat(seat);
		}

		bus.setBusName(busDetails.get("BusOperatorName"));
		bus.setRoute(busRouteRepo.findRoute(busDetails.get("BoardingPoint"), busDetails.get("DestinationPoint")));
		bus.setRoute(busroute);
		bus.setBusType(busDetails.get("busType"));
		bus.setPrice(busDetails.get("ticketPrice"));
		bus.setTotalSeats(bus.getTotalSeats() + Integer.parseInt(busDetails.get("AddSeats")));
		bus.setSeatsAvailable(bus.getSeatsAvailable() + Integer.parseInt(busDetails.get("AddSeats")));
		bus.setArrivalTime(LocalTime.parse(busDetails.get("ArrivalTime")));
		bus.setDepartureTime(LocalTime.parse(busDetails.get("DepartureTime")));
		busrepo.save(bus);

		return new ResponseEntity<>(new SuccessMessage("Bus Details Updation Success", HttpStatus.CREATED),
				HttpStatus.CREATED);
	}

	@PostMapping("/addNewRoute")
	public ResponseEntity<?> addbusRoute(@RequestBody AddStation addStation) {

		BusRouteModel routemodel = new BusRouteModel();
		routemodel.setBoardingPoint(addStation.getBoardingPoint());
		routemodel.setDestinationPoint(addStation.getDestinationPoint());

		addStation.getStations().forEach((String s) -> {
			BusStationModel b = new BusStationModel();
			b.setStationname(s);
			busStationRepo.save(b);
			routemodel.setStations(b);
		});

		busRouteRepo.save(routemodel);

		return new ResponseEntity<>(new SuccessMessage("Bus Route Added", HttpStatus.CREATED), HttpStatus.CREATED);
	}

	@PostMapping("/addBus")
	public ResponseEntity<?> addBus(@RequestBody Map<String, String> busDetails) {
		ArrayList<BusRouteModel> busroute = busRouteRepo.findRoute(busDetails.get("BoardingPoint"),
				busDetails.get("DestinationPoint"));
		if (busroute.size() == 0) {
			return new ResponseEntity<>(new ErrorMessage("Route Not Found", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		BusModel bus = new BusModel();
		bus.setBusName(busDetails.get("BusOperatorName"));
		bus.setBusType(busDetails.get("busType"));
		bus.setCurrentLocation(busDetails.get("currentLocation"));
		bus.setPrice(busDetails.get("ticketPrice"));
		bus.setArrivalTime(LocalTime.parse(busDetails.get("ArrivalTime")));
		bus.setDepartureTime(LocalTime.parse(busDetails.get("DepartureTime")));
		bus.setSeatsAvailable(Integer.parseInt(busDetails.get("TotalSeats")));
		bus.setTotalSeats(Integer.parseInt(busDetails.get("TotalSeats")));
		bus.setRoute(busroute);
		bus.setArrivalDate(LocalDate.parse(busDetails.get("ArrivalDate")));
		bus.setDepartureDate(LocalDate.parse(busDetails.get("departureDate")));
		bus.setSeatsBooked(0);
		busrepo.save(bus);
		for (int i = 0; i < Integer.parseInt(busDetails.get("TotalSeats")); i++) {
			BusSeatsModel newSeat = new BusSeatsModel();
			newSeat.setSeatType(busDetails.get("SeatType"));
			newSeat.setBus(bus);
			busSeatsRepo.save(newSeat);
			bus.addBusSeat(newSeat);
		}
		busrepo.save(bus);

		return new ResponseEntity<>(new SuccessMessage("BusDetails Addition Success", HttpStatus.CREATED),
				HttpStatus.CREATED);
	}

	@GetMapping("/bus/{id}")
	public ResponseEntity<?> getBusDetails(@PathVariable("id") String Id) {
		Optional<BusModel> bus = busrepo.findById(Long.parseLong(Id));
		return ResponseEntity.ok(bus);
	}

	@GetMapping("/getAllBuses")
	public ResponseEntity<?> getAllBusDetails() {
		List<BusModel> bus = busrepo.findAll();
		return ResponseEntity.ok(bus);
	}

	@GetMapping("/getAllRoutes")
	public ResponseEntity<?> getAllRouteDetails() {
		List<BusRouteModel> route = busRouteRepo.findAll();
		return ResponseEntity.ok(route);
	}

	@DeleteMapping("/deletebus/{id}")
	public ResponseEntity<?> deleteBus(@PathVariable("id") String Id) {
		bookingsrepo.deleteBookings(Long.parseLong(Id));
		busseatsrepo.deleteSeats(Long.parseLong(Id));
		busrepo.deleteById(Long.parseLong(Id));
		return new ResponseEntity<>(new SuccessMessage("Bus deletion Success", HttpStatus.OK), HttpStatus.OK);
	}

}
