package com.ticketbooking.main.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.main.models.UserModel;
import com.ticketbooking.main.service.UserServiceImpl;
import com.ticketbooking.main.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	JwtUtil jwtUtil;

//	@Autowired
//	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService userDetailsService;

	private ObjectMapper mapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");

		if (request.getServletPath().equals("/api/v1/auth/getAccessToken")

				|| request.getServletPath().equals("/api/v1/auth/signup")
				|| request.getServletPath().equals("/api/v1/auth/sigin")) {
			System.out.println("im in good track");
			filterChain.doFilter(request, response);
		}

		if (authorizationHeader == null) {
			System.out.println("No header");
			filterChain.doFilter(request, response);
			return;
		}

		try {

			String jwtToken = null;
			String username = null;

			String bearerToken = request.getHeader("Authorization");

			if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

				jwtToken = bearerToken.substring(7, bearerToken.length());
				username = jwtUtil.extractUsername(jwtToken);
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					System.out.println(jwtUtil.validateToken(jwtToken, userDetails));

					System.out.println(jwtUtil.validateToken(jwtToken, userDetails));
					if (jwtUtil.validateToken(jwtToken, userDetails)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, userDetails.getUsername(), userDetails.getAuthorities());

//							authenticationManager.authenticate(usernamePasswordAuthenticationToken);

						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}

			}

		} catch (Exception e) {
			System.out.println("caught");
		}

		filterChain.doFilter(request, response);
		return;
	}

}

//usernamePasswordAuthenticationToken
//.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));