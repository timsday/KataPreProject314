package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleService;

    public UserServiceImpl() {
    }

    @Override
    public void save(User user) {
        user.addRole(roleService.findRoleByRoleName("ROLE_USER"));
        userRepository.save(user);
    }

    @Override
    public User findByID(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void deleteByID(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().toList();
    }

    @Override
    public List<User> getSearchResultList(String keyword) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(keyword)
                        || user.getLastName().equalsIgnoreCase(keyword)
                        || user.getEmail().equalsIgnoreCase(keyword)
                        || user.getUsername().contains(keyword)
                        || user.getLastName().contains(keyword)
                        || user.getEmail().contains(keyword))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public void addRole(User user, String roleName) {
        if (roleName.equals("ROLE_USER") || roleName.equals("ROLE_ADMIN")) {
            user.addRole(roleService.findRoleByRoleName(roleName));
        }
    }
}
