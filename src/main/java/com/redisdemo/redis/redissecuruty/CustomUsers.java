package com.redisdemo.redis.redissecuruty;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

public class CustomUsers implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String username;
	public String password;
	public List<SimpleGrantedAuthority> authority;

	@Autowired
	public CustomUsers(String username,String password,String role) {
		this.username = username;
		this.password = password;
		this.authority = List.of(role).stream().map(SimpleGrantedAuthority::new).toList();

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub

		return this.authority;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.password;
	}

}
