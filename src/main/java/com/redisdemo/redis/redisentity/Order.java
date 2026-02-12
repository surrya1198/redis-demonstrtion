package com.redisdemo.redis.redisentity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.redisdemo.redis.redissecuruty.UserEntity;

import jakarta.persistence.*;
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

		@OneToMany(mappedBy = "order")
		List<OrderItem>orderItems=new ArrayList<>();
	}



