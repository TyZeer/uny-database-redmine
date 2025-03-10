package com.uny.unydatabaseredmine.auth.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class Employee implements UserDetails {
    @Id
    private Long id;
    private String name;
    private String job_title;
    private List<Role> roles;
    private String email;
    private String password;
    private String tgName;

    public Employee(String username, String job_title,String email, String password, String tgName) {
        this.name = username;
        this.email = email;
        this.password = password;
        this.job_title = job_title;
        this.tgName = tgName;
    }
    public Employee(Long id, String username, String job_title,String email, String password) {
        this.id = id;
        this.name = username;
        this.email = email;
        this.password = password;
        this.job_title = job_title;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName().name())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
