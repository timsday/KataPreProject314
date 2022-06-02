package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Validated
@Controller
@RequestMapping("/")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/login")
	public String getLoginPage() {
		return "login";
	}


	@GetMapping(value = "/signup")
	public String getSignUpPage(@ModelAttribute("user") User user) {
		return "signup";
	}

	@PostMapping("/signup/register")
	public String registerUser(@ModelAttribute("user") @Valid User user,
							   BindingResult bindingResult,
							   ModelMap model,
							   BCryptPasswordEncoder passwordEncoder) {
		if (userService.findUserByUsername(user.getUsername()) != null) {
			model.addAttribute("userAlreadyExists", true);
			return "signup";
		}
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.save(user);
		return "redirect:/login?signup";
	}

	@GetMapping("/logout")
	public String logoutProcess(HttpServletRequest request,
								HttpServletResponse response,
								Authentication auth) {
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
}