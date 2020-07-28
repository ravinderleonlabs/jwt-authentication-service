package com.leonlabs.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leonlabs.auth.entity.UserLoginActivity;

@Repository
public interface UserLoginActivityRepository extends JpaRepository<UserLoginActivity, Integer> {
	
}
