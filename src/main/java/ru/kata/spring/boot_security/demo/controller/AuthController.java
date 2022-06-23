package ru.kata.spring.boot_security.demo.controller;

import org.jetbrains.annotations.ApiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class AuthController {
	private final UserService userService;
	private final RoleService roleService;

	@Autowired
	public AuthController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
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
							   ModelMap model, ServletRequest request) {
		if (userService.findUserByUsername(user.getUsername()) != null) {
			model.addAttribute("userAlreadyExists", true);
			return "signup";
		}
		String[] selectedRoles = request.getParameterValues("selectedRoles");
		if (selectedRoles != null) {
			user.setRoles(Arrays.stream(selectedRoles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(user);
		return "redirect:/login?signup";
	}
}