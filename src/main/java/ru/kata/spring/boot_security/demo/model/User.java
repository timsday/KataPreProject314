package ru.kata.spring.boot_security.demo.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(unique = true)
   @NotEmpty(message = "Please, type name")
   @Size(min = 3, max=30, message = "Correct length: 3 — 30 chars")
   private String username;

   @NotEmpty(message = "Please, type last name")
   @Size(min = 3, max=30, message = "Correct length: 3 — 30 chars")
   private String lastName;

   @NotNull
   @Min(value = 0) @Max(value = 130)
   private Byte age;

   @NotEmpty(message = "Please, type email")
   @Email(message = "Please, type correct email")
   private String email;

   @Size(min = 4, message = "Min. 4 chars")
   private String password;

   @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
   @JoinTable(name = "user_roles",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<Role> roles = new LinkedHashSet<>();

   public User() {
   }

   public User(String username, String lastName, Byte age, String email, String password) {
      this.username = username;
      this.lastName = lastName;
      this.age = age;
      this.email = email;
      this.password = password;
   }

   public User(String username, String lastName, Byte age, String email, String password, Set<Role> roles) {
      this.username = username;
      this.lastName = lastName;
      this.age = age;
      this.email = email;
      this.password = password;
      this.roles = roles;
   }

   public Long getId() {
      return id;
   }
   public void setId(long id) {
      this.id = id;
   }
   public void setUsername(String username) {
      this.username = username;
   }
   public void setPassword(String password) {
      this.password = password;
   }
   public Byte getAge() {
      return age;
   }
   public void setAge(Byte age) {
      this.age = age;
   }
   public String getLastName() {
      return lastName;
   }
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }
   public String getEmail() {
      return email;
   }
   public void setEmail(String email) {
      this.email = email;
   }
   public Set<Role> getRoles() {
      return roles;
   }
   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   public boolean isAdmin() {
      return getRolesNamesString().contains("ADMIN");
   }

   public String getRolesNamesString() {
      List<String> rolesNamesList = roles.stream()
              .map(Role::getRoleName)
              .map(s -> s.replace("ROLE_", ""))
              .toList();
      StringBuilder rolesNamesString = new StringBuilder();
      for (String roleName : rolesNamesList) {
         rolesNamesString.append(" ").append(roleName);
      }
      return rolesNamesString.toString();
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return getRoles();
   }

   @Override
   public String getUsername() {
      return username;
   }
   @Override
   public String getPassword() {
      return password;
   }
   @Override
   public boolean isAccountNonExpired() {
      return true;
   }
   @Override
   public boolean isAccountNonLocked() {
      return true;
   }
   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }
   @Override
   public boolean isEnabled() {
      return true;
   }
}
