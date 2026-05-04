package com.redisdemo.redis.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto  {
	
	/**
	 * 
	 */
	private Long id;
    private String orderNumber;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> orderItems;

}
