package com.uny.unydatabaseredmine.auth.service;


import com.uny.unydatabaseredmine.auth.jwt.JwtUtils;
import com.uny.unydatabaseredmine.auth.models.Employee;
import com.uny.unydatabaseredmine.auth.repos.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtils jwtUtils;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with this email was not found"));

    }
    public String generateJwtToken(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }
}
