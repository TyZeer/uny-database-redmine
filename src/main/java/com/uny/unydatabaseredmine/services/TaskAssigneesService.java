package com.uny.unydatabaseredmine.services;

import com.uny.unydatabaseredmine.models.Severity;
import com.uny.unydatabaseredmine.models.TaskAssignees;
import com.uny.unydatabaseredmine.repos.TaskAssigneesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskAssigneesService {
    @Autowired
    TaskAssigneesRepository taskAssigneesRepository;

    public void save(Long taskId, Long projectId, Severity severity){
        taskAssigneesRepository.setAssignee(taskId, projectId, severity);
    }
    public Long getAssignee(Long taskId){
        Long employeeId = taskAssigneesRepository.getAssignedEmployees(taskId);
        return employeeId;
    }
}
