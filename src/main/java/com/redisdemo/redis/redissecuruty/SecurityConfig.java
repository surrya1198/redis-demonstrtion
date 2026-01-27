package com.redisdemo.redis.redissecuruty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.redisdemo.redis.jwtutils.JwtAuthFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Lazy
	@Autowired
	JwtAuthFilter authFilter;

	@Bean
	public SecurityFilterChain configSecurity(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/users/public/**").permitAll().anyRequest().authenticated())
				//.sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				.httpBasic(Customizer.withDefaults())
				.addFilterBefore(authFilter,UsernamePasswordAuthenticationFilter.class);
		        

		return http.build();

	}

	@Bean

	public PasswordEncoder passwordencoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authprovider() {

		DaoAuthenticationProvider autprovider = new DaoAuthenticationProvider();
		autprovider.setPasswordEncoder(passwordencoder());
		autprovider.setUserDetailsService(userDetailsService());
		return autprovider;
	}

	@Bean
	public AuthenticationManager authmanger(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService() {

		return new CustomUserDetailService();
	}

}
