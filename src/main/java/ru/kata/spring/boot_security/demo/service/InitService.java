package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;

@Component
public class InitService implements ApplicationRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

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
                (byte) 30,
                "user@user.com",
                "user"));
    }
}
