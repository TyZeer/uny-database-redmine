package com.uny.unydatabaseredmine.auth.service.interfaces;


import com.uny.unydatabaseredmine.auth.dto.LoginDto;
import com.uny.unydatabaseredmine.auth.dto.RegisterDto;
import com.uny.unydatabaseredmine.auth.models.Role;
import com.uny.unydatabaseredmine.auth.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface IUserService {
    String authenticate(LoginDto loginDto);
    ResponseEntity<?> register(RegisterDto registerDto);
    Role saveRole(Role role);
    User saveUser(User user);
}
