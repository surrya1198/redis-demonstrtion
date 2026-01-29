package com.redisdemo.redis.redisservice;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.redisdemo.redis.redisentity.Order;
import com.redisdemo.redis.redisrepo.OrderRepo;
import com.redisdemo.redis.redisrepo.UserRepo;
import com.redisdemo.redis.redissecuruty.UserEntity;

@Service
public class OrderService {

	@Autowired
	UserRepo userrepo;

	@Autowired
	OrderRepo orderRepo;

	public Order createOrder(Order order) {
		if (order != null) {

			order.setOrderNumber(UUID.randomUUID().toString());

			order.setTotalAmount(calculateTotalAmount());

			order.setOrderDate(LocalDateTime.now());

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			String username = auth.getName();

			UserEntity user = userrepo.findUserByName(username);
			if (user != null)
				order.setUser(user);

		}
		return orderRepo.save(order);

	}

	private Double calculateTotalAmount() {

		return Double.MIN_NORMAL;
	}
}
