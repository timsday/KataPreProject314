package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/")
public class UserPanelController {

	@Autowired
	private UserService userService;

	@GetMapping( "/user")
	public String printCurrentUserData(ModelMap model, Authentication auth) {
		model.addAttribute("currentUser",
				userService.findUserByUsername(auth.getName()));
		return "user";
	}
}