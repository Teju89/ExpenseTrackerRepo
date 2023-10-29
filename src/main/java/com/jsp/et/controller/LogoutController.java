package com.jsp.et.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LogoutController {

	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, RedirectAttributes attributes) {
		HttpSession session = request.getSession();
		//to close session
		session.invalidate();
		attributes.addFlashAttribute("msg", "LOGOUT SUCCESSFULLY");
		return "redirect:/expense/login";
	}
}
