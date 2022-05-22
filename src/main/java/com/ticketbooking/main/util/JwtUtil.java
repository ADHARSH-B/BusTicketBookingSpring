package com.ticketbooking.main.util;

//import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
	private static String SECRET_KEY = "secretkey"; // wher to save this
	private static String REFERSH_TOKEN_SECRET_KEY="somesupersecretkey";
//	@Value("${ACCESS_TOKEN_SECRET_KEY}")
//	private static String SECRET_KEY;
	
	public String extractUsername(String token) {
		return extractClaim(token).getSubject();
	}
	
	public String extractRefreshTokenUsername(String token) {
		return extractRefreshTokenClaim(token).getSubject();
	}

	public Date extractExpiration(String token) {
		return extractClaim(token).getExpiration();
	}
	
	public Date extractRefreshTokenExpiration(String token) {
		return extractRefreshTokenClaim(token).getExpiration();
	}

	public Claims extractClaim(String token) {
		return extractAllClaims(token);
	}
	
	public Claims extractRefreshTokenClaim(String token) {
		return extractAllRefreshTokenClaims(token);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
	}
	private Claims extractAllRefreshTokenClaims(String token) {
		
		return Jwts.parser().setSigningKey(REFERSH_TOKEN_SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
	}
	
	

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	private Boolean isRefreshTokenExpired(String token) {
		return extractRefreshTokenExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role",userDetails.getAuthorities());
		return createToken(claims, userDetails.getUsername());
	}
	


// how soon the tokens are supposed to be refreshed
	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()).compact();
	}
//	System.currentTimeMillis() + 1000 * 60 * 60 * 10

	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 20))
				.signWith(SignatureAlgorithm.HS256, REFERSH_TOKEN_SECRET_KEY.getBytes()).compact();
	}
	public boolean validateRefreshToken(String token,UserDetails userDetails) throws UnsupportedEncodingException{
		System.out.println("check");
		final String username = extractRefreshTokenUsername(token);
		System.out.println("----------");
		System.out.println(username);;
		return (username.equals(userDetails.getUsername()) && !isRefreshTokenExpired(token));
	}
	public Boolean validateToken(String token, UserDetails userDetails) throws UnsupportedEncodingException {
	
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}