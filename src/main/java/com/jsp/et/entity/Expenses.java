package com.jsp.et.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="expense_table")
public class Expenses {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int expenseId;
	private LocalDate date;
	private double amount;
	private String description;
	
	@ManyToOne //many records from expenses table associate with one record of user table
	@JoinColumn(name="user_fk")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="category_fk")
	private ExpenseCategory expenseCategory;
}









