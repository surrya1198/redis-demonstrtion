package com.redisdemo.redis.jwtutils;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.redisdemo.redis.redissecuruty.CustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	JwtServiceUtils jwt;

	@Autowired
	AuthenticationManager authmanager;

	@Autowired
	CustomUserDetailService userDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().contains("public")) {

			filterChain.doFilter(request, response);
			return;
		}
		
		if(request.getHeader("Authorization").startsWith("Basic")) {
			
			filterChain.doFilter(request, response);
			return;
		}

		String token = request.getHeader("Authorization");

		if (token.contains("Bearer")) {

			token = token.substring(7);
		}

		// check wether it is vaild token or not
		if (jwt.vaildtoken(token)) {

			UserDetails d = userDetails.loadUserByUsername(jwt.getClaimsSubject(token));
			UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(d, d.getPassword(), d.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			//auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			filterChain.doFilter(request, response);
		}
		

	}

}
