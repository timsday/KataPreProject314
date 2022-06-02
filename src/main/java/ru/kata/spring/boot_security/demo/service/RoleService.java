package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {

    void save(Role role);

    Role findByID(long id);

    Role findRoleByRoleName(String roleName);

    void delete(Role role);

    List<Role> findAll();

}
