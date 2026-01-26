package com.redisdemo.redis.redisservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.redisdemo.redis.redisentity.Product;
import com.redisdemo.redis.redisrepo.RedisRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService {
	@Autowired
	RedisRepo redisrepo;

	@CachePut(value = "products", key = "#result.id")

	public Product saveProduct(Product product) {

		if (product.getPrice() < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		} 
		if(redisrepo.findbyname(product.getName())!=null) {
			throw new IllegalArgumentException("Product with name "+product.getName()+" already exists");
		}
		else {
			Product savedProduct = new Product(product.getId(), product.getName(), product.getPrice());
			
			log.info("Product saved: " + savedProduct);
			redisrepo.save(savedProduct);
			return savedProduct;
		}

	}

	@Cacheable(value = "products", key = "#id")
	public Product getProductById(Long id) {

		if (id <= 0) {
			throw new IllegalArgumentException("ID must be positive");
		} else {
			log.info("Fetching product with {} from the db: " + id);
			Product product = redisrepo.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
			log.info("Product retrieved: " + product);
			return product;
		}
	}

	@CacheEvict(value = "products", key = "#id")
	public void deleteProductById(Long id) {
		if (id <= 0) {
			throw new IllegalArgumentException("ID must be positive");
		} else {
			redisrepo.deleteById(id);
			log.info("Product deleted with id: " + id);
		}
	}

	@CachePut(value = "products", key = "#result.id")
	public Product updateProduct(Long id, Product updatedProduct) {
		if (id <= 0) {
			throw new IllegalArgumentException("ID must be positive");
		}
		if (updatedProduct.getPrice() < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}

		Product existingProduct = redisrepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

		existingProduct.setName(updatedProduct.getName());
		existingProduct.setPrice(updatedProduct.getPrice());

		redisrepo.save(existingProduct);
		log.info("Product updated: " + existingProduct);
		return existingProduct;
	}

	public List<Product> saveAllProducts(List<Product> products) {
		for (Product product : products) {
			if (product.getPrice() < 0) {
				throw new IllegalArgumentException("Price cannot be negative for product: " + product.getName());
			}
		}
		List<Product> savedProducts = redisrepo.saveAll(products);
		log.info("All products saved: " + savedProducts);
		return savedProducts;
	}
	
	public List<Product> getallProduct(){
		
		 List<Product>products=redisrepo.findAll();
		 
		 if(products.isEmpty()) {
			 throw new IllegalArgumentException("There are no product avilable to display");
		 }
		 else {
			 return products;
		 }
		
		
	}
}
