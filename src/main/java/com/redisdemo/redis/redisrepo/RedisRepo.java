package com.redisdemo.redis.redisrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.redisdemo.redis.redisentity.Product;

@Repository
@EnableJpaRepositories
public interface RedisRepo extends JpaRepository<Product, Long> {
	
	@Query("SELECT a FROM Product a WHERE a.name= ?1")
	public Product findbyname(String name);

}
