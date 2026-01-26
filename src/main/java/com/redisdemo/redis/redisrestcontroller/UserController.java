package com.redisdemo.redis.redisrestcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo.redis.redissecuruty.UserEntity;
import com.redisdemo.redis.redisservice.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/public/createuser")
	public ResponseEntity<UserEntity> creteUserOrRegisterUser(@RequestBody UserEntity user) {

		if (user != null) {

			return ResponseEntity.ok(userService.createUserOrRegiste(user));

		}
		return null;

	}

	@GetMapping("/getUsers/{name}")
	public ResponseEntity<UserEntity> getUser(@PathVariable("name") String user) {

		if (user != null) {

			return ResponseEntity.ok(userService.getUser(user));

		}
		return null;

	}

}
