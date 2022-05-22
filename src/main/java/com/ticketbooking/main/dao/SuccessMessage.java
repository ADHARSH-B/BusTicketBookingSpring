package com.ticketbooking.main.dao;

import org.springframework.http.HttpStatus;

public class SuccessMessage {
	private String message;
	private HttpStatus errorCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}

	public SuccessMessage(String message, HttpStatus code) {
		super();
		this.message = message;
		this.errorCode = code;
	}

}
