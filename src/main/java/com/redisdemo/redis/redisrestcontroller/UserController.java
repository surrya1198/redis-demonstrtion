package com.redisdemo.redis.redisrestcontroller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo.redis.jwtutils.JwtServiceUtils;
import com.redisdemo.redis.redissecuruty.UserEntity;
import com.redisdemo.redis.redisservice.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	JwtServiceUtils jwtservice;
	
	@Autowired
	AuthenticationManager auth;

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

	@PostMapping("/public/genratetoken")
	public ResponseEntity<String> generateToken(@RequestBody UserEntity userDetails) {
		String token = null;
		if (userDetails.getUsername() != null) {

			Authentication authentication = auth.authenticate(new UsernamePasswordAuthenticationToken(
					userDetails.getUsername(), userDetails.getPassword(),
					List.of(userDetails.getRole()).stream().map(SimpleGrantedAuthority::new).toList()));

			if (authentication.isAuthenticated()) {
				token = jwtservice.genrateToken(userDetails.getUsername());
			}
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);

	}

}
