package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

	private final UserService userService;

	public AdminPanelController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping( "/users")
	public String printAllUsers(ModelMap model) {
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@GetMapping("/users/add")
	public String addUserForm(@ModelAttribute("user") User user) {
		return "users/add";
	}

	@PostMapping(value = "/addUser")
	public String addUserProcess(@ModelAttribute("user") @Valid User user,
								 BindingResult bindingResult,
								 BCryptPasswordEncoder passwordEncoder) {
		if (bindingResult.hasErrors()) {
			return "users/add";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.save(user);
		return "redirect:/admin/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUserForm(@PathVariable long id,
							   @ModelAttribute("role") String role,
							   ModelMap model) {
		model.addAttribute("user", userService.findByID(id));
		return "users/edit";
	}

	@PostMapping(value = "/editUserProcess")
	public String editUserProcess(@ModelAttribute("user")
								  @Valid User user, BindingResult bindingResult,
								  @ModelAttribute("role") String role,
								  BCryptPasswordEncoder passwordEncoder) {
		if (bindingResult.hasErrors()) {
			return "users/edit";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.addRole(user, role);
		userService.save(user);
		return "redirect:/admin/users";
	}

	@GetMapping("/deleteUserProcess{id}")
	public String deleteUserProcess(@PathVariable long id) {
		userService.deleteByID(id);
		return "redirect:/admin/users";
	}

	@GetMapping(value="/search")
	public String searchUserForm(@RequestParam(name="keyword") String keyword,
								 ModelMap modelMap) {
		modelMap.addAttribute("searchResultList",
				userService.getSearchResultList(keyword));
		return "search_results";
	}
}