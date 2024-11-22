package com.proj.recipe.service.interfaces;

import com.proj.recipe.dto.LoginDto;
import com.proj.recipe.dto.RegisterDto;
import com.proj.recipe.models.Role;
import com.proj.recipe.models.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    String authenticate(LoginDto loginDto);
    ResponseEntity<?> register(RegisterDto registerDto);
    Role saveRole(Role role);
    User saveUser(User user);
}
