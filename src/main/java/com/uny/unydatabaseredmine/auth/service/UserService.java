package com.proj.recipe.service;

import com.proj.recipe.dto.BearerToken;
import com.proj.recipe.dto.LoginDto;
import com.proj.recipe.dto.RegisterDto;
import com.proj.recipe.jwt.JwtUtils;
import com.proj.recipe.models.Role;
import com.proj.recipe.models.RoleName;
import com.proj.recipe.models.User;
import com.proj.recipe.repos.RoleRepository;
import com.proj.recipe.repos.UserRepository;
import com.proj.recipe.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AuthenticationManager authenticationManager ;
    private final UserRepository userRepository ;
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtUtils jwtUtilities ;
    @Override
    public String authenticate(LoginDto loginDto) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(authentication.getName());
        User user = userRepository.findUserByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r-> rolesNames.add(r.getRoleName().name()));
        String token = jwtUtilities.generateToken(user.getUsername(),rolesNames);
        return "User login successful! Token: " + token;

    }

    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("email is already taken !",
                    HttpStatus.SEE_OTHER);
        } else {
            User user = new User();
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerDto.
                    getPassword()));
            String myrole = "user";
            if (registerDto.getUserRole().isEmpty() || registerDto.
                    getUserRole().equals("user")) {
                myrole = "USER";
            }
            if (registerDto.getUserRole().equals("admin")) {
                myrole = "ADMIN";
            }
            Role role = roleRepository.findByRoleName(RoleName.valueOf(myrole.toUpperCase())).get();
            user.setUserRole(registerDto.getUserRole());
            user.setRoles(Collections.singletonList(role));
            userRepository.save(user);
            String token = jwtUtilities.generateToken(registerDto.getEmail(
            ), Collections.singletonList(role.getRoleName().name()));
            return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
        }
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
