package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final UserService userService;
	private final RoleService roleService;

	public AdminController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping( "/users")
	public String getUsers(ModelMap model, Authentication auth) {
		model.addAttribute("currentUser",
				userService.findUserByUsername(auth.getName()));
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@GetMapping("/add")
	public String createUserForm(@ModelAttribute("newUser") User newUser, ModelMap model,
							  Authentication auth) {
		model.addAttribute("currentUser",
				userService.findUserByUsername(auth.getName()));
		return "add";
	}

	@PostMapping(value = "/users")
	public String createUser(@ModelAttribute("newUser") @Valid User newUser,
								 ServletRequest request) {
		String[] roles = request.getParameterValues("newRoles");
		if (roles != null) {
			newUser.setRoles(Arrays.stream(roles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(newUser);
		return "redirect:/admin/users";
	}

	@PutMapping(value = "/users{id}")
	public String updateUser(@ModelAttribute("user") @Valid User user,
								  ServletRequest request) {
		String[] roles = request.getParameterValues("editRoles");
		if (roles != null) {
			user.setRoles(Arrays.stream(roles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(user);
		return "redirect:/admin/users";
	}

	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable long id) {
		userService.deleteByID(id);
		return "redirect:/admin/users";
	}
}