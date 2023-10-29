package com.jsp.et.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.et.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByUserNameAndPassword(String username, String password);
}
