package com.redisdemo.redis.redisrestcontroller;

import com.redisdemo.redis.redisentity.Order;
import com.redisdemo.redis.redisservice.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")

public class OrderController {
    @Autowired
    public OrderService orderService;

    @PostMapping("/placeorder")
    public ResponseEntity createOrder(@RequestBody Order order) {

        Order createdOrder=orderService.createOrder(order);
        return new ResponseEntity(createdOrder, HttpStatus.CREATED);
    }

	

	@PutMapping("/updateorder")
	public void updateOrder() {

	}

	@DeleteMapping("/cancelorder")
	public void deleteOrder() {

	}

	@GetMapping("/getorder")
	public void getOrder() {
	}

}
