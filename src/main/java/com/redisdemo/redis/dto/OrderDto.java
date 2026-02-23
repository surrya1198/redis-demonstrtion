package com.redisdemo.redis.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String orderNumber;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> orderItems;

}
