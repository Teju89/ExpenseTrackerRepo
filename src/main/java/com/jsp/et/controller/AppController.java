package com.jsp.et.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsp.et.dto.ExpensesDTO;
import com.jsp.et.service.ExpenseService;

@Controller
@RequestMapping("/expense")
public class AppController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/home")
	public String home() {
		return "Home";
	}

	// AppController
	@RequestMapping("/viewExpense")
	public String viewExpense(@ModelAttribute("listOfExpense") List<ExpensesDTO> expenses) {
		return "viewExpense";
	}

	@RequestMapping("/login")
	public String login(@ModelAttribute("msg") String message) {
		return "login";
	}

	@RequestMapping("/register")
	public String register(@ModelAttribute("msg") String message) {
		return "register";
	}

	// appController
	@RequestMapping("/addExpense")
	public String addExpense(@ModelAttribute("error") String message) {
		return "AddExpense";
	}

	@Autowired
	private ExpenseService service;

	// to display UpdateExpense page with expense details by using expenseId
	@RequestMapping("/updateExpense/{id}")
	public String updateExpense(@PathVariable("id") int id, Model m) {
		ExpensesDTO dto = service.findByExpenseId(id);
		m.addAttribute("expense", dto);
		return "UpdateExpense";
	}

	@RequestMapping("/filterExpense")
	public String filterExpense() {
		return "FilterExpense";
	}

	@RequestMapping("/forgotPassword")
	public String forgotPassword() {
		return "ForgotPassword";
	}
	
	@RequestMapping("/totalExpense")
	public String totalExpense() {
		return "TotalExpense";
	}
	
	@RequestMapping("/updateProfile")
	public String updateProfile() {
		return "UpdateProfile";
	}
}
