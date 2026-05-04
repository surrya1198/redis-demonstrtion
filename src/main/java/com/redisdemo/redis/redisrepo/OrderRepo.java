package com.redisdemo.redis.redisrepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.redisdemo.redis.redisentity.Order;

@EnableJpaRepositories
@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

	public Optional<Order> findById(Long id);

	// @Query("SELECT o FROM Orders o WHERE o.orderNumber= ?1")
	@Query(value = "SELECT * FROM redisschema.orders WHERE order_number = ?1", nativeQuery = true)
	public List<Order> findByOrderNumber(String orderNumber);

	@Modifying
	@Transactional
	@Query(value = "DELETE from redisschema.order_item oi USING redisschema.orders o WHERE o.order_number=oi.order_number AND oi.order_number= ?1 ",

			nativeQuery = true)
	public void deletebyorderNumber(String orderNumber);

}
