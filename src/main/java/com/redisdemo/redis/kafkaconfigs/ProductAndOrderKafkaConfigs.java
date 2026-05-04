package com.redisdemo.redis.kafkaconfigs;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductAndOrderKafkaConfigs {

	public NewTopic orderTopic() {
		return new NewTopic("orderdetail-to-vendor", 2, (short) 1);

	}
	
	public NewTopic orderCancellationTopic() {
		return new NewTopic("ordercancelation-to-vendor", 2, (short) 1);

	}

}
