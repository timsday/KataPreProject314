package ru.kata.spring.boot_security.demo.service;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;

@Service
public class InitService implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;
    public InitService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public void run(ApplicationArguments args) {
        roleService.save(new Role("ROLE_ADMIN"));
        roleService.save(new Role("ROLE_USER"));
        userService.save(new User("admin",
                "admin",
                (byte) 30,
                "admin@admin.com",
                "admin",
                Set.of(roleService.findRoleByRoleName("ROLE_ADMIN"),
                        roleService.findRoleByRoleName("ROLE_USER"))));
        userService.save(new User("user",
                "user",
                (byte) 35,
                "user@user.com",
                "user",
                Set.of(roleService.findRoleByRoleName("ROLE_USER"))));
    }
}
