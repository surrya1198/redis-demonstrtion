package com.redisdemo.redis.kafkaproducers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.redisdemo.redis.dto.OrderDto;

@Component
public class OrderMessageProducers {

	@Autowired
	public KafkaTemplate<String, Object> ordertovendorproducer;

	public void sendOrderDeatilsToVendorService(List<OrderDto> orderDetails) {
        
		ObjectMapper mapper=new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
		ordertovendorproducer.send("orderdetails-to-vendor", orderDetails);

	}
	
	
	public void sendOrderCancelattionDetailsToVendorService(String ordernumber) {
		
		ObjectMapper mapper=new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
		ordertovendorproducer.send("ordercancelation-to-vendor", ordernumber);

	}

}
