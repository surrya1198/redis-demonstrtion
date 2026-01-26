package com.redisdemo.redis.redissecuruty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.redisdemo.redis.redisrepo.UserRepo;

@Component
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity user = userRepo.findUserByName(username);

		if (user == null) {
			throw new IllegalArgumentException("user not found");

		}
		CustomUsers user1 = new CustomUsers(user.getUsername(),user.getPassword(),user.getRole());
		return user1;
	}

}
