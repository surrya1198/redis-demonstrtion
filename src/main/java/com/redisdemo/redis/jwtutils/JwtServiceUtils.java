package com.redisdemo.redis.jwtutils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.redisdemo.redis.redissecuruty.CustomUserDetailService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtServiceUtils {
	
	@Autowired
	CustomUserDetailService userdetailsService;
	
	@Value("${jwt.secret}")
	public String secret;
	

	Map<String, ?> claim = new HashMap<>();

	public String genrateToken(String username) {
		String token = Jwts.builder().subject(username).claims(claim)
				.expiration(new Date(System.currentTimeMillis() + 1000 * 180)).issuedAt(new Date()).signWith(signkey())
				.compact();
		return token;
	}

	public Key signkey() {

		byte[] keys = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keys);
	}

	boolean vaildtoken(String token) {
		
		if(token!=null) {
			
			String userName=getClaimsSubject(token);
			
			UserDetails user=userdetailsService.loadUserByUsername(userName);
			
			if(user!=null && getClaimsExpDate(token).after(new Date())) {
				
				return true;
			}
				
			
			
			
		}
		return false;

	}

	String getClaimsSubject(String token) {
		return Jwts.parser().verifyWith((SecretKey) signkey()).build().parseSignedClaims(token).getPayload().getSubject();
	}
	Date getClaimsExpDate(String token) {
		return Jwts.parser().verifyWith((SecretKey) signkey()).build().parseSignedClaims(token).getPayload().getExpiration();
	}

	

}
