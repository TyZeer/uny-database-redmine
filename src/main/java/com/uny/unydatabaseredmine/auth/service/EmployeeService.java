//package com.uny.unydatabaseredmine.auth.service;
//
//
//import com.uny.unydatabaseredmine.auth.dto.BearerToken;
//import com.uny.unydatabaseredmine.auth.dto.LoginDto;
//import com.uny.unydatabaseredmine.auth.dto.RegisterDto;
//import com.uny.unydatabaseredmine.auth.jwt.JwtUtils;
//import com.uny.unydatabaseredmine.auth.models.Employee;
//import com.uny.unydatabaseredmine.auth.models.Role;
//import com.uny.unydatabaseredmine.auth.models.RoleName;
//import com.uny.unydatabaseredmine.auth.repos.RoleRepository;
//import com.uny.unydatabaseredmine.auth.repos.EmployeeRepository;
//import com.uny.unydatabaseredmine.auth.service.interfaces.IEmployeeService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//public class EmployeeService implements IEmployeeService {
//    private final AuthenticationManager authenticationManager ;
//    private final EmployeeRepository employeeRepository;
//    private final RoleRepository roleRepository ;
//    private final PasswordEncoder passwordEncoder ;
//    private final JwtUtils jwtUtilities ;
//    @Override
//    public String authenticate(LoginDto loginDto) {
//        Authentication authentication= authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getEmail(),
//                        loginDto.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        log.info(authentication.getName());
//        Employee employee = employeeRepository.findUserByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        List<String> rolesNames = new ArrayList<>();
//        employee.getRoles().forEach(r-> rolesNames.add(r.getRoleName().name()));
//        String token = jwtUtilities.generateToken(employee.getUsername(),rolesNames);
//        return "User login successful! Token: " + token;
//
//    }
//
//    @Override
//    public ResponseEntity<?> register(RegisterDto registerDto) {
//
//        if (employeeRepository.existsByEmail(registerDto.getEmail())) {
//            return new ResponseEntity<>("email is already taken !",
//                    HttpStatus.SEE_OTHER);
//        } else {
//            Employee employee = new Employee();
//            employee.setEmail(registerDto.getEmail());
//            employee.setPassword(passwordEncoder.encode(registerDto.
//                    getPassword()));
//            String myrole = "user";
//            if (registerDto.getUserRole().isEmpty() || registerDto.
//                    getUserRole().equals("user")) {
//                myrole = "USER";
//            }
//            if (registerDto.getUserRole().equals("admin")) {
//                myrole = "ADMIN";
//            }
//            Role role = roleRepository.findByRoleName(RoleName.valueOf(myrole.toUpperCase())).get();
//            employee.setRoles(List.of(role));
//            employee.setRoles(Collections.singletonList(role));
//            employeeRepository.save(employee);
//            String token = jwtUtilities.generateToken(registerDto.getEmail(
//            ), Collections.singletonList(role.getRoleName().name()));
//            return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
//        }
//    }
//
//    @Override
//    public Role saveRole(Role role) {
//        return roleRepository.save(role);
//    }
//
//    @Override
//    public Employee saveEmployee(Employee employee) {
//        return employeeRepository.save(employee);
//    }
//}
