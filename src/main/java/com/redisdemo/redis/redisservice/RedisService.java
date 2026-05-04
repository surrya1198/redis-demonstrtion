package com.redisdemo.redis.redisservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.redisdemo.redis.exception.ProductAlreadyExistException;
import com.redisdemo.redis.exception.ProductNotFoundException;
import com.redisdemo.redis.redisentity.Product;
import com.redisdemo.redis.redisrepo.RedisRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService {
	@Autowired
	RedisRepo redisrepo;

	// @CachePut(value = "products", key = "#result.id")

	public Product saveProduct(Product product) {

		if (product.getPrice() < 0) {
			throw new ProductAlreadyExistException("Price cannot be negative",HttpStatus.FORBIDDEN);
		}
		Product productfromDb = redisrepo.findbyname(product.getName());
		if (productfromDb != null && productfromDb.getVendorcode() != null && productfromDb.getName() != null) {
			throw new ProductAlreadyExistException(
					"Product with name " + product.getName()
							+ " already exists or product cannot be stored without vendorcode",
					HttpStatus.NOT_ACCEPTABLE);
		} else {
			Product savedProduct = new Product(product.getId(), product.getName(), product.getPrice(),
					product.getVendorcode());
			if (productfromDb != null && product.getVendorcode().equals(productfromDb.getVendorcode())) {
				throw new ProductAlreadyExistException("Product already added with the same vendor",
						HttpStatus.NOT_ACCEPTABLE);

			}
			log.info("Product saved: " + savedProduct);
			redisrepo.save(savedProduct);
			return savedProduct;
		}

	}

	// @Cacheable(value = "products", key = "#id")
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
			throw new ProductNotFoundException("ID must be positive", HttpStatus.OK);
		}
		if (updatedProduct.getPrice() < 0) {
			throw new ProductNotFoundException("Price cannot be negative", HttpStatus.OK);
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
				throw new ProductNotFoundException("Price cannot be negative for product: " + product.getName(),
						HttpStatus.OK);
			}
		}
		List<Product> savedProducts = redisrepo.saveAll(products);
		log.info("All products saved: " + savedProducts);
		return savedProducts;
	}

	public List<Product> getallProduct() {

//		Pageable p = PageRequest.of(0, 0,Sort.by(Order.asc("id")));
//		List<Product> products = redisrepo.findAll(p).getContent();
		List<Product> products = redisrepo.findAll();

		if (products.isEmpty()) {
			throw new ProductNotFoundException("There are no product avilable to display", HttpStatus.OK);
		} else {
			return products;
		}

	}
}
