package com.jsp.et.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.et.entity.Expenses;
import com.jsp.et.entity.User;

public interface ExpenseRepository extends JpaRepository<Expenses, Integer>{

	//to find list of expenses based on user details
	List<Expenses> findByUser(User user);
	
	List<Expenses> findByDateBetween(LocalDate start, LocalDate end);
}
