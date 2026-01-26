package com.redisdemo.redis.redisservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.redisdemo.redis.redisrepo.UserRepo;
import com.redisdemo.redis.redissecuruty.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    
	@Autowired
	private UserRepo repo;
	
	@Autowired
	PasswordEncoder enocEncoder;

	
	public UserService(UserRepo repo) {
		this.repo = repo;
	}

	public UserEntity createUserOrRegiste(UserEntity user) {

		UserEntity userdb = repo.findUserByName(user.getUsername());
		if (userdb != null) {

			throw new IllegalArgumentException("username already exists");
		} else {
			log.info("user saved successfully");
			user.setPassword(enocEncoder.encode(user.getPassword()));
			return repo.save(user);
		}

	}

	public UserEntity getUser(String user) {

		UserEntity userdb = repo.findUserByName(user);
		if (userdb == null) {

			throw new IllegalArgumentException("username doesnot exists");
		} else {
			log.info("user fecthed successfully successfully");
			return repo.findUserByName(user);
		}

	}

}
