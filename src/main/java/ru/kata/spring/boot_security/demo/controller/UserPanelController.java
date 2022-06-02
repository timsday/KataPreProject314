package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/")
public class UserPanelController {

	private final UserService userService;

	public UserPanelController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping( "/user")
	public String printCurrentUserData(ModelMap model,
									   Authentication authentication) {
		model.addAttribute("user",
				userService.findUserByUsername(authentication.getName()));
		return "user";
	}
}