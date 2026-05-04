package com.redisdemo.redis.kafkaconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redisdemo.redis.redisentity.Product;
import com.redisdemo.redis.redisservice.RedisService;

@Component
public class ProductListenerFromVendorService1 {

	@Autowired
	RedisService productservice;

	@KafkaListener(topics = "vendorToTopic", groupId = "vendortoproduct-group-1")
	public void pickUptheproductFromTheBroker(String product) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Product p = new Product();
			try {
				p = mapper.readValue(product, Product.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (product != null) {
				productservice.saveProduct(p);
			}
		} catch (Exception e) {

		}
	}
}
