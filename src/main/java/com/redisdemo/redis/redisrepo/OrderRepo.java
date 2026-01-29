package com.redisdemo.redis.redisrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.redisdemo.redis.redisentity.Order;

@EnableJpaRepositories
@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

}
