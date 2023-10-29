package com.jsp.et.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.et.dto.ExpensesDTO;
import com.jsp.et.entity.ExpenseCategory;
import com.jsp.et.entity.Expenses;
import com.jsp.et.entity.User;
import com.jsp.et.repository.ExpenseCategoryRepository;
import com.jsp.et.repository.ExpenseRepository;
import com.jsp.et.repository.UserRepository;

/**
 * 
 * @author Tejal Borase
 *
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private ExpenseCategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Expense table contains two foreign keys - categoryId & UserId To insert value
	 * in foreign keys columns, firstly programmer have to verify user & category
	 * then need to retrieve it by using name and id After that insert into Expense
	 * table.
	 */
	@Override
	public int addExpense(ExpensesDTO dto, int userId) {

		// Verification and retrieval of category by using its name
		Optional<ExpenseCategory> category = categoryRepository.findByCategory(dto.getCategory());

		// Verification and retrieval of user by using its id
		Optional<User> user = userRepository.findById(userId);

		// if both are valid then insert record in table
		if (category.isPresent() && user.isPresent()) {
			Expenses expenses = new Expenses();

			// convert String of DTO to LocalDate of Entity
			expenses.setDate(LocalDate.parse(dto.getDate()));

			BeanUtils.copyProperties(dto, expenses);
			expenses.setExpenseCategory(category.get());
			expenses.setUser(user.get());

			return expenseRepository.save(expenses).getExpenseId();
		}
		return 0;
	}

	/*
	 * To get all the expenses based on userId because every user have different
	 * expenses, Retrieve Expenses by using userId.
	 */
	@Override
	public List<ExpensesDTO> viewExpense(int userId) {
		// 1. Find user details in User table based on id then retrieve object
		User user = userRepository.findById(userId).get();

		// created user-defined method in expenserepository to access expenses by using
		// user-details

		/*
		 * findByUser returns list of expenses entity object, so to copy data from
		 * expenses entity list to expenseDTO list make use of stream api or foreach
		 * loop
		 */
		return expenseRepository.findByUser(user).stream().map(t -> {
			ExpensesDTO dto = new ExpensesDTO();
			BeanUtils.copyProperties(t, dto);
			// to store category from ExpenseCategory Object to ExpensesDto
			/*
			 * t.getExpenseCategory() gives object of ExpenseCategory entity class
			 */
			dto.setCategory(t.getExpenseCategory().getCategory());

			// convert LocalDate of Entity class into String of DTO
			dto.setDate(t.getDate().toString());

			return dto;
		}).collect(Collectors.toList());
	}

	/*
	 * To get expenses based on its id
	 */
	@Override
	public ExpensesDTO findByExpenseId(int id) {
		Optional<Expenses> expensedb = expenseRepository.findById(id);
		if (expensedb.isPresent()) {
			ExpensesDTO dto = new ExpensesDTO();
			BeanUtils.copyProperties(expensedb.get(), dto);
			dto.setCategory(expensedb.get().getExpenseCategory().getCategory());

			// convert LocalDate to String
			dto.setDate(expensedb.get().getDate().toString());
			return dto;
		}
		return null;
	}

	/*
	 * to update expenses details
	 */
	@Override
	public ExpensesDTO updateExpense(ExpensesDTO dto, int expenseId) {
		// 1. Find expense by using its id
		Expenses expense = expenseRepository.findById(expenseId).get();
		// 2. Verification
		if (expense != null) {
			// 3. update data in retrieved object of Expense
			expense.setAmount(dto.getAmount());

			// convert string to LocalDate
			expense.setDate(LocalDate.parse(dto.getDate()));
			expense.setDescription(dto.getDescription());

			// 4. find category from category table based on its name then update in expense
			ExpenseCategory category = categoryRepository.findByCategory(dto.getCategory()).get();
			expense.setExpenseCategory(category);

			// 5. update expense by using save method
			ExpensesDTO updated = new ExpensesDTO();
			BeanUtils.copyProperties(expenseRepository.save(expense), updated);
			return updated;
		}
		return null;
	}

	/*
	 * to delete expenses based on id
	 */
	@Override
	public int deleteExpense(int expenseId) {
		// 1. find expense based on id
		Optional<Expenses> expenseDB = expenseRepository.findById(expenseId);
		// 2. Verification
		if (expenseDB.isPresent()) {
			// 3. deletion
			expenseRepository.delete(expenseDB.get());
			return 1;
		}
		return 0;
	}

	/*
	 * It will retrieve data from db based on matching category amount and date make
	 * use of filter method from stream api
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnDateCategoryAmount(ExpensesDTO dto, int userId) {
		/*
		 * call viewExpense method because its contains the logic, to get all expenses
		 * of respective user, so programmer have to just filter expenses of user as per
		 * requirement
		 */
		return viewExpense(userId).stream().filter(t -> t.getDate().equals(dto.getDate())
				&& t.getAmount() == dto.getAmount() && t.getCategory().equals(dto.getCategory())).toList();
	}

	/*
	 * It will retrieve data from db based on date make use of filter method from
	 * stream api
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnDate(ExpensesDTO dto, int userId) {
		return viewExpense(userId).stream().filter(t -> t.getDate().equals(dto.getDate())).toList();
	}

	/*
	 * It will retrieve data from db based on matching category make use of filter
	 * method from stream api
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnCategory(ExpensesDTO dto, int userId) {
		return viewExpense(userId).stream().filter(t -> t.getCategory().equals(dto.getCategory())).toList();
	}

	/*
	 * It will retrieve data from db based on matching range of amount use of filter
	 * method from stream api Take range in format of string "100-200"
	 */
	@Override
	public List<ExpensesDTO> filterBasedOnAmount(String range, int userId) {
		String[] arr = range.split("-");

		return viewExpense(userId).stream().filter(t -> {
			int start = Integer.parseInt(arr[0]);
			int end = Integer.parseInt(arr[1]);
			return start <= t.getAmount() && end >= t.getAmount();
		}).collect(Collectors.toList());
	}

	/* It will find total of expenses between given date for respective user */
	@Override
	public List<ExpensesDTO> filterExpenseBasedOnDate(LocalDate start, LocalDate end, int userId) {
		/*
		 * 1. get all expenses for respective user 2. make use of stream api to filter
		 * expenses based on given start and end data
		 * 
		 */
		return viewExpense(userId).stream().filter(t -> {
			LocalDate date = LocalDate.parse(t.getDate());
			return !date.isBefore(start) && !date.isAfter(end);
		}).collect(Collectors.toList());
	}

}




