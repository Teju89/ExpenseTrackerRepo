package com.jsp.et.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.et.dto.ExpensesDTO;
import com.jsp.et.dto.ImageDTO;
import com.jsp.et.dto.TotalDTO;
import com.jsp.et.dto.UserDTO;
import com.jsp.et.service.ExpenseService;
import com.jsp.et.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ExpenseService expenseService;

	@PostMapping("/registeration")
	public String registration(@ModelAttribute UserDTO userDTO, Model m, RedirectAttributes attributes) {
		int id = userService.registration(userDTO);
		if (id != 0) {
			// display login page
			// redirect request to login method
			m.addAttribute("msg", "Registration Successful!!!");
			attributes.addFlashAttribute("msg", "REGISTRATION SUCCESSFULL!!");
			return "redirect:/expense/login";
		}
		// display registration page
		m.addAttribute("msg", "PLEASE ENTER SAME PASSWORD....");
		attributes.addFlashAttribute("msg", "PLEASE ENTER VALID DETAILS...");
		return "redirect:/expense/register";
	}

	@PostMapping("/loginOperation")
	public String login(@ModelAttribute UserDTO userDTO, RedirectAttributes attributes, HttpServletRequest request) {
		UserDTO dto = userService.login(userDTO);
		//System.out.println(dto.getImage().getId());
		if (dto != null) {
			// to store users data in session object
			request.getSession().setAttribute("user", dto);
			if(dto.getImage() != null) {
				
				/**
				 * store image in session object but in the form of String.
				 * By using Base64 class present in java.util package - programmer can encode
				 * byte data to String
				 */
				request.getSession().setAttribute("image", 
						Base64.getMimeEncoder().encodeToString(dto.getImage().getData()));
			}
			return "redirect:/expense/home";
		}
		attributes.addFlashAttribute("msg", "PLEASE ENTER VALID CREDENTIALS...");
		return "redirect:/expense/login";
	}

	@PostMapping("/addexpense/{id}")
	public String addExpense(@ModelAttribute ExpensesDTO dto, @PathVariable("id") int userId,
			RedirectAttributes attributes) {
		int id = expenseService.addExpense(dto, userId);
		System.out.println(userId);
		if (id > 0) {
			// redirect request to viewExpense method from UserController and concat userId
			return "redirect:/viewexpense/" + userId;
		}
		attributes.addFlashAttribute("error", "PLEASE ENTER VALID DETAILS...");
		return "redirect:/expense/addExpense";
	}

	// UserController
	@GetMapping("/viewexpense/{id}")
	public String viewExpense(@PathVariable("id") int userId, RedirectAttributes attributes) {
		System.out.println(userId);
		List<ExpensesDTO> expenses = expenseService.viewExpense(userId);
		attributes.addFlashAttribute("listOfExpense", expenses);
		return "redirect:/expense/viewExpense";
	}

	@PostMapping("/updateexpense/{eid}")
	public String updateExpense(@ModelAttribute ExpensesDTO dto, @PathVariable("eid") int expenseId,
			HttpServletRequest request) {
		ExpensesDTO expense = expenseService.updateExpense(dto, expenseId);
		if (expense != null) {
			UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
			return "redirect:/viewexpense/" + userDTO.getUserId();
		}
		return "redirect:/expense/home";
	}

	@GetMapping("/deleteexpense/{eid}")
	public String deleteExpense(@PathVariable("eid") int expenseId, HttpServletRequest request) {
		System.out.println(expenseId);
		int id = expenseService.deleteExpense(expenseId);
		if (id != 0) {
			// Make use of Session object to get user Id
			UserDTO dto = (UserDTO) request.getSession().getAttribute("user");
			// redirect to viewexpense method to display expenses by using userID;
			return "redirect:/viewexpense/" + dto.getUserId();
		}
		return "redirect:/expense/home";
	}

	@GetMapping("/filter/{userid}")
	public String filterExpenseByCategoryDateAmount(@ModelAttribute ExpensesDTO dto, @PathVariable("userid") int userid,
			RedirectAttributes attributes) {

		if (!dto.getRange().equalsIgnoreCase("0")) {
			List<ExpensesDTO> filterByAmount = expenseService.filterBasedOnAmount(dto.getRange(), userid);
			attributes.addFlashAttribute("listOfExpense", filterByAmount);
			return "redirect:/expense/viewExpense";
		} else if (dto.getCategory() != "") {
			List<ExpensesDTO> filterByCategory = expenseService.filterBasedOnCategory(dto, userid);
			attributes.addFlashAttribute("listOfExpense", filterByCategory);
			return "redirect:/expense/viewExpense";
		} else if (dto.getDate() != "") {
			List<ExpensesDTO> filterByDate = expenseService.filterBasedOnDate(dto, userid);
			attributes.addFlashAttribute("listOfExpense", filterByDate);
			return "redirect:/expense/viewExpense";
		}
		return "redirect:/expense/home";
	}

	@GetMapping("/total/{userId}")
	public String getTotalOfExpense(@ModelAttribute TotalDTO total, @PathVariable("userId") int userId, Model m) {

		List<ExpensesDTO> dto = expenseService.filterExpenseBasedOnDate(LocalDate.parse(total.getStart()),
				LocalDate.parse(total.getEnd()), userId);

		m.addAttribute("listOfExpense", dto);
		m.addAttribute("total", dto.stream().mapToDouble(t -> t.getAmount()).sum());

		return "viewExpense";
	}

	@PostMapping("/updateuser/{id}")
	public String updateUser(@PathVariable("id") int id, @ModelAttribute UserDTO dto,
							 @RequestParam("imageFile") MultipartFile file, HttpServletRequest request) {
		try {
			/**
			 * retrieve UserDTO object from session object, store at the time of Login
			 */
			UserDTO fromSession= (UserDTO)request.getSession().getAttribute("user");
			//if user already have uploaded profile photo then update the photo
			if(fromSession.getImage() != null) {
				//updation logic
				fromSession.getImage().setData(file.getBytes());
				dto.setImage(fromSession.getImage());
				
				//store same image in session object
				request.getSession().setAttribute("image", 
						Base64.getMimeEncoder().encodeToString(dto.getImage().getData()));
			}
			else {
			//if user uploading profile photo first time 
				ImageDTO imageDto = new ImageDTO();
				imageDto.setData(file.getBytes());
				dto.setImage(imageDto);
			}
			
			userService.updateUserProfile(id, dto);
			return "redirect:/expense/home";
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/expense/home";
	}
	
	@DeleteMapping("/deleteuser/{id}")
	public ResponseEntity<Integer> deleteUser(@PathVariable("id") int id) {
		int status = userService.deleteUserProfile(id);
		if (status != 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(status);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
	}
}
