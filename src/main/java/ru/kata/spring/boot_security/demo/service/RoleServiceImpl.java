package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }
    @Override
    public Role findRoleByRoleName(String role) {
        return roleRepository.findRoleByRoleName(role);
    }
    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
