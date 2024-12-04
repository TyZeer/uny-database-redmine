package com.uny.unydatabaseredmine.services;

import com.uny.unydatabaseredmine.auth.models.Employee;
import com.uny.unydatabaseredmine.auth.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findUserByEmail(email).orElse(null);
    }

}
