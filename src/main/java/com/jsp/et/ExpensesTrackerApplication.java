package com.jsp.et;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpensesTrackerApplication implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(ExpensesTrackerApplication.class, args);
	}
//	@Autowired
//	private ExpenseCategoryRepository repository;
	@Override
	public void run(String... args) throws Exception {
//		//to execute any code at the starting of application
//		List<ExpenseCategory> categories = Arrays.asList(
//				new ExpenseCategory("utilities"),
//				new ExpenseCategory("Transportation"),
//				new ExpenseCategory("Groceries"),
//				new ExpenseCategory("Dining out"),
//				new ExpenseCategory("HealthCare"),
//				new ExpenseCategory("Entertainment"),
//				new ExpenseCategory("Education"),
//				new ExpenseCategory("Personal Care"),
//				new ExpenseCategory("Clothing"),
//				new ExpenseCategory("Home Maintaince"),
//				new ExpenseCategory("Gifts And Donations"),
//				new ExpenseCategory("Savings & Investments"),
//				new ExpenseCategory("Tax"),
//				new ExpenseCategory("Others")
//				);
//		//store all list elements in the database
//		repository.saveAll(categories);
	}

}














