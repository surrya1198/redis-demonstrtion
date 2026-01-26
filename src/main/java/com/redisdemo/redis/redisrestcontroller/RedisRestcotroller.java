package com.redisdemo.redis.redisrestcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo.redis.redisentity.Product;
import com.redisdemo.redis.redisservice.RedisService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/redis")
public class RedisRestcotroller {

	private final RedisService redisservice;

	public RedisRestcotroller(RedisService redisservice) {
		this.redisservice = redisservice;

	}
	
	@PostMapping("/createproducts")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		Product savedProduct = redisservice.saveProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}
	
	@GetMapping("/products/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = redisservice.getProductById(id);
		return ResponseEntity.ok(product);
	}
	@DeleteMapping("/deleteproducts/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
		redisservice.deleteProductById(id);
		return ResponseEntity.noContent().build();
	}
	@PutMapping("/updateproducts/{id}")
	public ResponseEntity<Product> updateProduct(@PathParam("id") Long id, @RequestBody Product updatedProduct) {
		Product product = redisservice.updateProduct(id, updatedProduct);
		return ResponseEntity.ok(product);
	}
	@PostMapping("/allproducts")
	public ResponseEntity<List<Product>> postAllProducts(@RequestBody List<Product> products) {
		List<Product> products1 = redisservice.saveAllProducts(products);
		return ResponseEntity.status(HttpStatus.CREATED).body(products1);
	}
	
	@GetMapping("/getallproducts")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<Product>>getallproducts(){
		
		return ResponseEntity.ok(redisservice.getallProduct());
	}
	
	

}
