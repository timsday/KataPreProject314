package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
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
								 BindingResult bindingResult,
								 ServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "add";
		}
		String[] selectedRoles = request.getParameterValues("selectedRoles");
		if (selectedRoles != null) {
			newUser.setRoles(Arrays.stream(selectedRoles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(newUser);
		return "redirect:/admin/users";
	}

	//Modal dialog used instead (Bootstrap task)
	/*@GetMapping("/users/edit/{id}")
	public String editUserForm(@PathVariable long id,
							   @ModelAttribute("role") String role,
							   ModelMap model) {
		model.addAttribute("user", userService.findByID(id));
		return "users/edit";
	}*/

	@PostMapping(value = "/editUserProcess")
	public String editUserProcess(@ModelAttribute("user") @Valid User user,
								  BindingResult bindingResult,
								  ServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "users";
		}
		String[] selectedRoles = request.getParameterValues("selectedRoles");
		if (selectedRoles != null) {
			user.setRoles(Arrays.stream(selectedRoles)
					.map(roleService::findRoleByRoleName)
					.collect(Collectors.toSet()));
		}
		userService.save(user);
		return "redirect:/admin/users";
	}

	@GetMapping("/deleteUserProcess{id}")
	public String deleteUserProcess(@PathVariable long id) {
		userService.deleteByID(id);
		return "redirect:/admin/users";
	}

	//Not used in Bootstrap task (Bootstrap task)
	/*@GetMapping(value="/search")
	public String searchUserForm(@RequestParam(name="keyword") String keyword,
								 ModelMap modelMap) {
		modelMap.addAttribute("searchResultList",
				userService.getSearchResultList(keyword));
		return "search_results";
	}*/
}