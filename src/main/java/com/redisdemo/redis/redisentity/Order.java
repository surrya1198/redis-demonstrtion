package com.redisdemo.redis.redisentity;

import java.time.LocalDateTime;

import com.redisdemo.redis.redissecuruty.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders",schema = "redisschema")
@Data
@Setter
@Getter
public class Order {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(unique = true,nullable = false)
	    private String orderNumber;
	    @Column(nullable = false)
	    private Double totalAmount;
	    
	    @Column(nullable = false)
	    private LocalDateTime orderDate;

	    

		@ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "users",referencedColumnName = "id")
	    private UserEntity user;
	}



