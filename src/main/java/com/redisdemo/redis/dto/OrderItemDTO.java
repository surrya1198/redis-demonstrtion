package com.redisdemo.redis.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OrderItemDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long productId;
	private String productName;
	private Integer quantity;
	private Double price;
	private String vendorcode;
}
