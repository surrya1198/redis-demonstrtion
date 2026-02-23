package com.redisdemo.redis.redisentity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redisdemo.redis.redissecuruty.UserEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders", schema = "redisschema")

@JsonIgnoreProperties
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Column(unique = true, nullable = false)
	private String orderNumber;

	@Column(nullable = false)
	private Double totalAmount;

	@Column(nullable = false)
	private LocalDateTime orderDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users", referencedColumnName = "id")
	private UserEntity user;

	@OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
	List<OrderItem> orderItems = new ArrayList<>();
}
