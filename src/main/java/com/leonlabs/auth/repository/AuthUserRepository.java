package com.leonlabs.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonlabs.auth.entity.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    
	AuthUser findByUsername(String username);

	AuthUser findByUsernameAndPassword(String userName, String password);
}
