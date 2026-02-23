package com.redisdemo.redis.redisservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.redisdemo.redis.dto.OrderDto;
import com.redisdemo.redis.dto.OrderItemDTO;
import com.redisdemo.redis.redisentity.Order;
import com.redisdemo.redis.redisentity.OrderItem;
import com.redisdemo.redis.redisentity.Product;
import com.redisdemo.redis.redisrepo.OrderRepo;
import com.redisdemo.redis.redisrepo.UserRepo;
import com.redisdemo.redis.redissecuruty.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private UserRepo userrepo;

	private OrderRepo orderRepo;

	private RedisService redisservice;

	public OrderService(UserRepo userrepo, OrderRepo orderRepo, RedisService redisservice) {
		this.userrepo = userrepo;
		this.orderRepo = orderRepo;
		this.redisservice = redisservice;
	}

	@CachePut(value = "orders", key = "#result.id")
	public OrderDto createOrder(List<Integer> listofproducts) {

		Order order = null;

		if (listofproducts != null) {
			order = new Order();
			order.setOrderNumber(UUID.randomUUID().toString());

			order.setOrderDate(LocalDateTime.now());

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			UserDetails user=(UserDetails) auth.getPrincipal();
			
			UserEntity user1 = userrepo.findUserByName("admin");
			if (user1 != null)
				order.setUser(user1);

			if (listofproducts != null) {
				List<OrderItem> orderItem = this.processOrder(listofproducts,order);
				order.getOrderItems().addAll(orderItem);
				Double totalAmount = orderItem.stream().mapToDouble(p -> p.getProduct().getPrice()).sum();
				order.setTotalAmount(totalAmount);
			}
   			 else {
				throw new IllegalArgumentException("List of products cannot be null");
			}
		}
		Order savedOrder = orderRepo.save(order);
		log.info("Order created: " + order);
		return convertToDto(savedOrder);

	}

	public List<OrderItem> processOrder(List<Integer> listofproducts,Order order) {
		List<OrderItem> orderItems = new ArrayList<>();

		for (Integer productId : listofproducts) {
			OrderItem orderItem = new OrderItem();
			Product prod = redisservice.getProductById(Long.valueOf(productId));

			orderItem.setProduct(prod);
			orderItem.setOrder(order);

			orderItems.add(orderItem);
			
			
		}
		return orderItems;
	}

	public void updateOrder() {

	}

	public void deleteOrder() {

	}

	public void getOrder() {
	}
	
	
	public OrderDto convertToDto(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setOrderNumber(order.getOrderNumber());
		orderDto.setTotalAmount(order.getTotalAmount());
		orderDto.setOrderDate(order.getOrderDate());
		
		List<OrderItemDTO> orderItems = order.getOrderItems().stream().map(oi->{
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setProductId(oi.getProduct().getId());
			orderItemDTO.setProductName(oi.getProduct().getName());
			orderItemDTO.setPrice(oi.getProduct().getPrice());
			orderItemDTO.setQuantity(1);
			return orderItemDTO;
		}).collect(Collectors.toList());
		orderDto.setOrderItems(orderItems);
		return orderDto;
	}


}
