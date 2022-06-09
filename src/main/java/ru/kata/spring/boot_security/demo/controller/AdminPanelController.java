package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminPanelController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@GetMapping( "/users")
	public String printAllUsers(ModelMap model, Authentication auth) {
		model.addAttribute("currentUser",
				userService.findUserByUsername(auth.getName()));
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@GetMapping("/add")
	public String addUserForm(@ModelAttribute("newUser") User newUser, ModelMap model,
							  Authentication auth) {
		model.addAttribute("currentUser",
				userService.findUserByUsername(auth.getName()));
		return "add";
	}

	@PostMapping(value = "/addUser")
	public String addUserProcess(@ModelAttribute("newUser") @Valid User newUser,
								 ServletRequest request) {
		String[] selectedRoles = request.getParameterValues("selectedRoles");
		if (selectedRoles != null) {
			newUser.setRoles(Arrays.stream(selectedRoles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(newUser);
		return "redirect:/admin/users";
	}

	@PutMapping(value = "/editUserProcess{id}")
	public String editUserProcess(@ModelAttribute("user") @Valid User user,
								  ServletRequest request) {
		String[] selectedRoles = request.getParameterValues("selectedRoles");
		if (selectedRoles != null) {
			user.setRoles(Arrays.stream(selectedRoles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(user);
		return "redirect:/admin/users";
	}

	@DeleteMapping("/deleteUserProcess{id}")
	public String deleteUserProcess(@PathVariable long id) {
		userService.deleteByID(id);
		return "redirect:/admin/users";
	}
}