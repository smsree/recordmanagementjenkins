package com.axisbank.project2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axisbank.project2.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	//email as user name
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	void deleteByUsername(String username);
	
	
}
