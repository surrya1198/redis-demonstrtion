package com.redisdemo.redis.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductAlreadyExistException extends RuntimeException {

	String message;
	HttpStatus status;

	public ProductAlreadyExistException(String message, HttpStatus status) {

		this.message = message;
		this.status = status;
	}

}
