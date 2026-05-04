package com.redisdemo.redis.globalhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.redisdemo.redis.exception.OrderNotFoundException;
import com.redisdemo.redis.exception.ProductAlreadyExistException;
import com.redisdemo.redis.exception.ProductNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<?> handleOrderException(OrderNotFoundException ex) {

		ResponseEntity<?> error = new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
		return error;

	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handleproductException(ProductNotFoundException ex) {

		ResponseEntity<?> error = new ResponseEntity(ex.getmessage(), HttpStatus.NOT_FOUND);
		return error;

	}

	@ExceptionHandler(ProductAlreadyExistException.class)
	public ResponseEntity<?> handleProductExistException(ProductAlreadyExistException ex) {

		ResponseEntity<?> productAlreadyExist = new ResponseEntity(ex.getMessage(), ex.getStatus());
		return productAlreadyExist;
	}

}
