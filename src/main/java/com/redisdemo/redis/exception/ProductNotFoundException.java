package com.redisdemo.redis.exception;

import org.springframework.http.HttpStatusCode;

public class ProductNotFoundException extends RuntimeException {

	public String message;

	public HttpStatusCode statuscode;

	public ProductNotFoundException(String message, HttpStatusCode statuscode) {
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
