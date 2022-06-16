package ru.kata.spring.boot_security.demo.controller;


import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RestApiController {
	private final UserService userService;
	private final RoleService roleService;

	public RestApiController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping( "/users")
	public List<User> getUsers() {
		return userService.findAll();
	}

	@GetMapping( "/user")
	public User getCurrentUser(@NotNull Authentication auth) {
		return userService.findUserByUsername(auth.getName());
	}

	@GetMapping("/users/{id}")
	public User getUser(@PathVariable long id) {
		User user = userService.findByID(id);
		if (user == null) {
			throw new NoSuchElementException(
					"No such user with id = '" + id + "' in database.");
		}
		return user;
	}

	@PostMapping("/users")
	public void createUser(@RequestBody User newUser) {
		newUser.setRoles(Arrays.stream(newUser.getStringRoles())
				.map(roleService::findRoleByRoleName)
				.collect(Collectors.toSet()));
		userService.save(newUser);
	}

	@PutMapping("/users")
	public void updateUser(@RequestBody @Valid User user) {
		if (user == null) {
			throw new NoSuchElementException(
					"No such user with id = '" + "' in database.");
		}
		user.setRoles(Arrays.stream(user.getStringRoles())
				.map(roleService::findRoleByRoleName)
				.collect(Collectors.toSet()));
		userService.save(user);
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable long id) {
		if (userService.findByID(id) == null) {
			throw new NoSuchElementException(
					"No such user with id = '" + id + "' in database.");
		}
		userService.deleteByID(id);
	}
}