package com.redisdemo.redis.redisservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.redisdemo.redis.dto.OrderDto;
import com.redisdemo.redis.dto.OrderItemDTO;
import com.redisdemo.redis.kafkaproducers.OrderMessageProducers;
import com.redisdemo.redis.redisentity.Order;
import com.redisdemo.redis.redisentity.OrderItem;
import com.redisdemo.redis.redisentity.Product;
import com.redisdemo.redis.redisrepo.OrderRepo;
import com.redisdemo.redis.redisrepo.UserRepo;
import com.redisdemo.redis.redissecuruty.UserEntity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private UserRepo userrepo;

	private OrderRepo orderRepo;

	private RedisService redisservice;

	private OrderMessageProducers orderMessageProducer;

	public OrderService(UserRepo userrepo, OrderRepo orderRepo, RedisService redisservice,
			OrderMessageProducers ordermessageproduce) {
		this.userrepo = userrepo;
		this.orderRepo = orderRepo;
		this.redisservice = redisservice;
		this.orderMessageProducer = ordermessageproduce;
	}

	// @CachePut(value = "orders", key = "#result.id")
	public List<OrderDto> createOrder(List<Integer> listofproducts) {

		List<Order> OrderList = new LinkedList<Order>();
		if (listofproducts != null) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			// UserDetails user = (UserDetails) auth.getPrincipal();

			UserEntity user1 = userrepo.findUserByName("admin");
			if (user1 != null)

				if (listofproducts != null) {
					List<OrderItem> orderItem = this.processOrder(listofproducts);

					Set<String> distinctVendorcode = orderItem.stream()
							.filter(oi -> oi.getProduct().getVendorcode() != null).map(oi -> {
								return oi.getProduct().getVendorcode();
							}).distinct().collect(Collectors.toSet());

					distinctVendorcode.forEach((eachvendorcode) -> {
						Order neworder = new Order();
						List<OrderItem> vendorSpecificOrderItem = orderItem.stream()
								.filter(order1 -> order1.getProduct().getVendorcode().equals(eachvendorcode))
								.collect(Collectors.toList());

						neworder.setOrderDate(LocalDateTime.now());
						neworder.setOrderItems(vendorSpecificOrderItem);
						neworder.setOrderNumber(UUID.randomUUID().toString());
						neworder.setTotalAmount(
								vendorSpecificOrderItem.stream().mapToDouble(p -> p.getProduct().getPrice()).sum());

						vendorSpecificOrderItem.stream().forEach((oi) -> {
							oi.setOrder(neworder);
						});
						neworder.setUser(user1);
						OrderList.add(neworder);

					});

					/*
					 * process the order based on the vendor code if the same vendorcode same ordrer
					 * if different orderid sholube different
					 */

				} else {
					throw new IllegalArgumentException("List of products cannot be null");
				}
		}
		List<Order> savedOrder = orderRepo.saveAllAndFlush(OrderList);
		log.info("Order created: ........");
		List<OrderDto> listOfOrderDto = convertToDto(savedOrder);

		this.notifyTheVendorRegardingtheOrder(listOfOrderDto);
		return listOfOrderDto;
		// return savedOrderDto;

	}

	public List<OrderItem> processOrder(List<Integer> listofproducts) {
		List<OrderItem> orderItems = new ArrayList<>();

		for (Integer productId : listofproducts) {
			OrderItem orderItem = new OrderItem();
			Product prod = redisservice.getProductById(Long.valueOf(productId));

			orderItem.setProduct(prod);
			orderItems.add(orderItem);

		}
		// orderRepo.save(order);
		return orderItems;
	}

	public List<OrderDto> updateOrder(List<Integer> listofproducts, String ordernumber) {

		List<Order> order = orderRepo.findByOrderNumber(ordernumber);

		for (int i = 0; i <= order.size(); i++) {
			order.get(i).setOrderDate(LocalDateTime.now());
			order.get(i).setOrderNumber(ordernumber);
			order.get(i).getOrderItems().clear();
			if (listofproducts != null) {

				for (Integer productId : listofproducts) {
					OrderItem orderItem = new OrderItem();
					Product prod = redisservice.getProductById(Long.valueOf(productId));

					orderItem.setProduct(prod);
					orderItem.setOrder(order.get(i));

					order.get(i).getOrderItems().add(orderItem);

				}
				Double amount = order.get(i).getOrderItems().stream().map(t -> (Double) t.getProduct().getPrice())
						.reduce((a, b) -> a + b).get();
				order.get(i).setTotalAmount(amount);
			}
		}
		// orderRepo.saveAllAndFlush(order);
		return this.convertToDto(orderRepo.saveAll(order));

	}
	@CircuitBreaker(name = "updateRegardingCancelation", fallbackMethod = "updatecancelationtomessagequeue")
	public void cancelOrder(String Ordernumber) {

		if (Ordernumber != null) {
			List<Order> order = orderRepo.findByOrderNumber(Ordernumber);

			if (order != null) {
				List<OrderDto> ordersDelete = this.convertToDto(order);
				this.updatevendorRegardingCancellation(Ordernumber, ordersDelete);
			}

			if (order != null) {
				orderRepo.deletebyorderNumber(Ordernumber);
			}
		}

	}

	public List<OrderDto> getOrderByNumber(String OrderNumber) {

		if (OrderNumber != null) {
			List<Order> order = orderRepo.findByOrderNumber(OrderNumber);

			if (order != null) {
				return convertToDto(order);
			}
		}
		return null;

	}

	public List<OrderDto> convertToDto(List<Order> orders) {
		List<OrderDto> orderDtos = new LinkedList<>();
		for (Order order : orders) {

			OrderDto orderDto = new OrderDto();
			orderDto.setId(order.getId());
			orderDto.setOrderNumber(order.getOrderNumber());
			orderDto.setTotalAmount(order.getTotalAmount());
			orderDto.setOrderDate(order.getOrderDate());

			List<OrderItemDTO> orderItems = order.getOrderItems().stream().map(oi -> {
				OrderItemDTO orderItemDTO = new OrderItemDTO();
				orderItemDTO.setProductId(oi.getProduct().getId());
				orderItemDTO.setProductName(oi.getProduct().getName());
				orderItemDTO.setPrice(oi.getProduct().getPrice());
				orderItemDTO.setVendorcode(oi.getProduct().getVendorcode());
				orderItemDTO.setQuantity(1);
				return orderItemDTO;
			}).collect(Collectors.toList());
			orderDto.setOrderItems(orderItems);
			orderDtos.add(orderDto);
		}
		return orderDtos;
	}

	public void notifyTheVendorRegardingtheOrder(List<OrderDto> order) {
		orderMessageProducer.sendOrderDeatilsToVendorService(order);
	}

	
	private void updatevendorRegardingCancellation(String ordernumber, List<OrderDto> orders) {

		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		CollectionLikeType liketype = mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class,
				OrderDto.class);

		restTemplate.put(
				"http://localhost:6679/api/vendor/orderdetails/updatecancelledorder?ordernumber=" + ordernumber + "",
				orders);

	}

	public void updatecancelationtomessagequeue(String ordernumber,Exception ex) {
           System.out.println("As the system is down the request is sent to the q.....");
           orderMessageProducer.sendOrderCancelattionDetailsToVendorService(ordernumber);
	}

}
