package com.redisdemo.redis.redisrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.redisdemo.redis.redissecuruty.UserEntity;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<UserEntity, Long> {
	
	@Query("SELECT u FROM UserEntity u WHERE u.username= ?1")
	public UserEntity findUserByName(String name);

}
