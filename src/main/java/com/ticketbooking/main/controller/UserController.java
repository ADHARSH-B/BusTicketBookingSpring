package com.ticketbooking.main.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ticketbooking.main.repository.bookingsRepo;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketbooking.main.dao.ErrorMessage;
import com.ticketbooking.main.dao.SuccessMessage;
import com.ticketbooking.main.models.BusModel;
import com.ticketbooking.main.models.BusSeatsModel;
import com.ticketbooking.main.repository.BusRepo;
import com.ticketbooking.main.repository.BusRouteRepo;
import com.ticketbooking.main.repository.BusSeatsRepo;

import com.ticketbooking.main.repository.UserRepo;
import com.ticketbooking.main.models.BookingsModel;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import com.ticketbooking.main.dao.TokenResponse;
import com.ticketbooking.main.dao.BookTickets;
import com.lowagie.text.DocumentException;
import com.ticketbooking.main.dao.AuthResponse;
import com.ticketbooking.main.service.EmailSenderService;
import com.ticketbooking.main.service.PDFGeneratorService;
import com.ticketbooking.main.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	@Autowired
	private UserRepo userrepo;

	@Autowired
	AuthResponse userSuccess;

	@Autowired
	PDFGeneratorService pdfService;

	@Autowired
	AuthenticationManager authenticationmanager;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	EmailSenderService senderService;

	@Autowired
	private bookingsRepo bookingsrepo;

	@Autowired
	BusRouteRepo busRouteRepo;

	@Autowired
	private BusRepo busrepo;

	@Autowired
	bookingsRepo bookingrepo;

	@Autowired
	private BusSeatsRepo busSeatsRepo;

//	@GetMapping("/welcome")
//	public ResponseEntity<String> Welcome() {
//		return ResponseEntity.ok("Welcome " + SecurityContextHolder.getContext().getAuthentication().getName());
//	}

	@PostMapping("/getAccessToken")
	public ResponseEntity<?> getAccessToken(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		final String authorizationHeader = request.getHeader("Authorization");

		System.out.println("im in refresh token");
		if (authorizationHeader == null) {
			return new ResponseEntity<>(new ErrorMessage("Token Not Found !!", HttpStatus.NOT_FOUND),
					HttpStatus.NOT_FOUND);
		}

		String jwtToken = null;
		String username = null;

		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			jwtToken = bearerToken.substring(7, bearerToken.length());
			username = jwtUtil.extractUsername(jwtToken);

			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println(userDetails.getUsername());
				if (jwtUtil.validateToken(jwtToken, userDetails)) {
					String accessToken = jwtUtil.generateToken(userDetails);
					String refreshToken = jwtUtil.generateRefreshToken(userDetails);
					return new ResponseEntity<>(new TokenResponse(accessToken, refreshToken), HttpStatus.ACCEPTED);
				}
			}
		}
		return new ResponseEntity<>(new ErrorMessage("Token is not valid!!", HttpStatus.NOT_FOUND),
				HttpStatus.NOT_FOUND);

	}

	@GetMapping("/check")
	public ResponseEntity<?> check() {
		return ResponseEntity.ok("done");
	}

	@PostMapping("/bookTickets")
	public ResponseEntity<?> bookTicket(@RequestBody BookTickets bookTickets) {
		System.out.println(bookTickets);
		BookingsModel bookingsmodel = new BookingsModel();
		BusModel bus = busrepo.findById((long) bookTickets.getBusId()).get();
		bookTickets.getSeats().forEach(seats -> {
			BusSeatsModel seat = busSeatsRepo.findById((long) seats).get();
			seat.setBooked(true);
			seat.setUser(userrepo.findByuserName(bookTickets.getUserName()));
			seat.setBus(bus);
			bookingsmodel.addBookedSeats(seat);
			busSeatsRepo.save(seat);
		});
		bus.setSeatsBooked(bus.getSeatsBooked() + bookTickets.getSeats().size());
		bus.setSeatsAvailable(bus.getSeatsAvailable() - bookTickets.getSeats().size());
		bookingsmodel.setTotalCost(Integer.parseInt(bookTickets.getTotalCost()));
		bookingsmodel.setBusDetails(bus);
		bookingsmodel.setUserDetails(userrepo.findByuserName(bookTickets.getUserName()));
		bookingsmodel.setBookingDate(LocalDateTime.now());
		bookingsrepo.save(bookingsmodel);
		
		System.out.println("Hi Booked ticket");
		busrepo.save(bus);

		return new ResponseEntity<>(new SuccessMessage("Tickets booked", HttpStatus.OK), HttpStatus.OK);

	}

	@GetMapping("/pdf/generate/{id}")
	public void generatePdf(@PathVariable("id") String Id, HttpServletResponse response) {
		Optional<BookingsModel> b = bookingrepo.findById(Long.parseLong(Id));
		System.out.println(b.get());
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-inline";
		String headerValue = "attachment;filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		try {
			this.pdfService.export(response, b.get());
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("error caught");
		}
	}

	@GetMapping("/sendMail")
	public void sendMail() {
		try {
			senderService.sendMail("adharsh.ee18@bitsathy.ac.in", "Ticket Book Confirmed",
					"Your Ticket Booking is Confirmed");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}

	@GetMapping("/searchBuses")
	public ResponseEntity<?> searchBuses(@RequestParam("boardingPoint") String boardingPoint,
			@RequestParam("destinationPoint") String destinationPoint,
			@RequestParam("departureDate") String departureDate) {
		return ResponseEntity.ok(busrepo.findAllBusesByBoardingPointAndDestinationPoint(boardingPoint, destinationPoint,
				LocalDate.parse(departureDate)));
	}

	@GetMapping("/getBookings/{userName}")
	public ResponseEntity<?> getBookings(@PathVariable("userName") String user) {
		System.out.println("im in user");
		List<BookingsModel> bookings = bookingrepo.findByUserName(user);
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/getAllRoutes")
	public ResponseEntity<?> getALLRoutes() {
		return ResponseEntity.ok(busRouteRepo.findAll());
	}

}
