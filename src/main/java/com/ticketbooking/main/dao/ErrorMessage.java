package com.ticketbooking.main.dao;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import lombok.AllArgsConstructor;

@Component
public class ErrorMessage {
	private String message;

	private HttpStatus statusCode;

	public ErrorMessage() {

	}

	public ErrorMessage(String message, HttpStatus notFound) {
		this.message = message;
		this.statusCode = notFound;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

}
