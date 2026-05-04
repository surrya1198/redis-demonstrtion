package com.redisdemo.redis.redisrestcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo.redis.dto.OrderDto;
import com.redisdemo.redis.redisrepo.OrderRepo;
import com.redisdemo.redis.redisservice.OrderService;

@RestController
@RequestMapping("/api/order")

public class OrderController {

    private final OrderRepo orderRepo;
	@Autowired
	public OrderService orderService;

    OrderController(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

	@PostMapping("/placeorder")
	public ResponseEntity createOrder(@RequestBody List<Integer> listofproducts) {

		List<OrderDto> createdOrder = orderService.createOrder(listofproducts);
		return new ResponseEntity(createdOrder, HttpStatus.CREATED);
	}

	@PutMapping("/updateorder")
	public ResponseEntity<OrderDto> updateOrder(@RequestBody List<Integer> listofproducts, @RequestParam(value = "ordernumber") String id) {

		List<OrderDto> o = orderService.updateOrder(listofproducts, id);
		ResponseEntity<OrderDto> responseEntity = new ResponseEntity(o, HttpStatus.OK);
		return responseEntity;

	}

	@DeleteMapping("/cancelorder/{id}")
	public ResponseEntity<?> cancelOrder(@PathVariable("id") String ordernumber) {
		orderService.cancelOrder(ordernumber);
        ResponseEntity<?>a=new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return a;
	}

	@GetMapping("/getorder")
	public ResponseEntity<OrderDto> getOrder(@RequestParam String ordernumber) {
		
		List<OrderDto> o= orderService.getOrderByNumber(ordernumber);
		ResponseEntity<OrderDto>e=new ResponseEntity(o, HttpStatus.OK);
		return e;
		
	}

}
