package com.ticketbooking.main.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketbooking.main.dao.AuthRequest;
import com.ticketbooking.main.dao.AuthResponse;
import com.ticketbooking.main.dao.ErrorMessage;
import com.ticketbooking.main.dao.SuccessMessage;
import com.ticketbooking.main.dao.TokenResponse;
import com.ticketbooking.main.models.UserModel;
import com.ticketbooking.main.repository.RoleRepo;
import com.ticketbooking.main.repository.UserRepo;
import com.ticketbooking.main.service.EmailSenderService;
import com.ticketbooking.main.util.JwtUtil;

import org.springframework.security.core.userdetails.UserDetailsService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
	@Autowired
	private UserRepo userrepo;
	
	@Value("${FrontendAddress}")
	String frontEnd;
	

	@Autowired
	private RoleRepo rolerepo;

	@Autowired
	AuthenticationManager authenticationmanager;

	@Autowired
	EmailSenderService emailService;

	@Autowired
	AuthResponse userSuccess;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<?> sigUpuser(@RequestBody UserModel usermodel) {
		System.out.println("im in signup");
		UserModel user = userrepo.findByuserName(usermodel.getUserName());
		System.out.println(user);
		
		if (user != null) {
			return new ResponseEntity<>(
//					return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK).body("User Already Registered Please Sign In!!");
					new ErrorMessage("User Already Registered Please Sign In!!", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}

		if (userrepo.findByemail(usermodel.getEmail()) != null) {
			System.out.println("hey");
			return new ResponseEntity<>(
//					return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.OK).body("User Already Registered Please Sign In!!");
					new ErrorMessage("Email is already registered with us please try a different one",
							HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		usermodel.setPassword(encoder.encode(usermodel.getPassword()));
//		usermodel.addRoles(rolerepo.findById((long) 3).get());
		usermodel.addRoles(rolerepo.findById((long) 0).get());
		userrepo.save(usermodel);
		return ResponseEntity.ok().body(usermodel);
	}
	

	@PostMapping("/getAccessToken")
	public ResponseEntity<?> getAccessToken(@RequestBody Map<String, String> token)
			throws UnsupportedEncodingException {
		final String authorizationHeader = token.get("refreshToken");
		if (authorizationHeader == null) {

			return new ResponseEntity<>(new ErrorMessage("Token Not Found !!", HttpStatus.NOT_FOUND),
					HttpStatus.NOT_FOUND);
		}

		String jwtToken = null;
		String username = null;

		String bearerToken = token.get("refreshToken");
		System.out.println(bearerToken);
		System.out.println(bearerToken.startsWith("refresh"));
		if (StringUtils.hasText(bearerToken))  {
			jwtToken = bearerToken;
//			System.out.println("checkedin");
			username = jwtUtil.extractRefreshTokenUsername(jwtToken);
//			System.out.println("checkedout");

			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println(userDetails.getUsername());
//				jwtUtil.validateToken(jwtToken, userDetails)
				
				if (jwtUtil.validateRefreshToken(jwtToken, userDetails)) {
					String accessToken = jwtUtil.generateToken(userDetails);
					String refreshToken = jwtUtil.generateRefreshToken(userDetails);
					return new ResponseEntity<>(new TokenResponse(accessToken, refreshToken), HttpStatus.ACCEPTED);
				}
			}
		}
		System.out.println("finished");
		return new ResponseEntity<>(new ErrorMessage("Token is not valid!!", HttpStatus.NOT_FOUND),
				HttpStatus.NOT_FOUND);

	}

	@PostMapping("/signin")
	public ResponseEntity<?> signInuser(@RequestBody AuthRequest authrequest) {

		String username = authrequest.getUserName();
		String password = authrequest.getPassword();
		UserDetails userDetails = null;

		try {
			userDetails = userDetailsService.loadUserByUsername(username);
		} catch (Exception e) {
			return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found Please SignUp");
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		authenticationmanager.authenticate(usernamePasswordAuthenticationToken);

		userSuccess.setAuthToken(jwtUtil.generateToken(userDetails));
		userSuccess.setRefreshToken(jwtUtil.generateRefreshToken(userDetails));
		userSuccess.setMessage("Successfully Authenticated");
		userSuccess.setUsername(username);
		userSuccess.setRole(userDetails.getAuthorities());
		return ResponseEntity.ok(userSuccess);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody Map<String, String> token) {
		UserModel u=userrepo.findByresetToken(token.get("token"));
		
		if(u==null) {
			return new ResponseEntity<>(new ErrorMessage("Token is not valid", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		
		if(u.getResetTokenExpiry().isBefore(LocalTime.now())) {
			u.setResetToken(null);
			u.setResetTokenExpiry(null);
			userrepo.save(u);
			return new ResponseEntity<>(new ErrorMessage("Token is Expired", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		u.setPassword(encoder.encode(token.get("password")));
		u.setResetToken(null);
		u.setResetTokenExpiry(null);
		userrepo.save(u);
		
		return new ResponseEntity<>(new SuccessMessage("Password Updated", HttpStatus.CREATED), HttpStatus.CREATED);
	}

	@PostMapping("/reset-password-request")
	public ResponseEntity<?> passwordResetRequest(@RequestBody Map<String, String> user) throws MessagingException {
		UserModel u = userrepo.findByemail(user.get("email"));
		if (u == null) {
			return new ResponseEntity<>(new ErrorMessage("Email Not Registered with us", HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
		String token = UUID.randomUUID().toString();
//		String body ="<p>Your Password reset link is <a href='".concat( frontEnd).concat("/resetpassword?token=%s'>link</a><p>\"");
		System.out.println(frontEnd);
		String body =String.format("<p>Your Password reset link is <a href='http://mybusbooking-frontend.s3-website.ap-south-1.amazonaws.com/"
				+ "resetpassword?token=%s'>link</a><p>",token);
		System.out.println(body);
		emailService.sendMail(u.getEmail(), "Reset-Password-link", body);
		u.setResetToken(token);
		u.setResetTokenExpiry(LocalTime.now().plusMinutes(6));
		userrepo.save(u);
		return new ResponseEntity<>(new SuccessMessage("Email Sent", HttpStatus.CREATED), HttpStatus.CREATED);
	}

}
