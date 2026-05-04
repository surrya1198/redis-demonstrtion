package com.redisdemo.redis.exception;

import org.springframework.http.HttpStatusCode;

public class OrderNotFoundException extends RuntimeException {

	public String message;

	public HttpStatusCode statuscode;

	public OrderNotFoundException(String message, HttpStatusCode statuscode) {
		super();
		this.message = message;
		this.statuscode = statuscode;
	}

	public String getmessage() {

		return message;
	}

	public HttpStatusCode getstatus() {

		return statuscode;
	}

}
