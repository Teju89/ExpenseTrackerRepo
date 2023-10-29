package com.jsp.et.service;

import java.time.LocalDate;
import java.util.List;

import com.jsp.et.dto.ExpensesDTO;

public interface ExpenseService {

	int addExpense(ExpensesDTO dto, int userId);
	
	List<ExpensesDTO> viewExpense(int userId);
	
	ExpensesDTO findByExpenseId(int id);
	
	ExpensesDTO updateExpense(ExpensesDTO dto, int expenseId);
	
	int deleteExpense(int expenseId);
	
	List<ExpensesDTO> filterBasedOnDateCategoryAmount(ExpensesDTO dto, int userId);
	
	List<ExpensesDTO> filterBasedOnDate(ExpensesDTO dto, int userId);
	
	List<ExpensesDTO> filterBasedOnCategory(ExpensesDTO dto, int userId);
	
	List<ExpensesDTO> filterBasedOnAmount(String range, int userId);
	
	List<ExpensesDTO> filterExpenseBasedOnDate(LocalDate start, LocalDate End, int userId);
}















