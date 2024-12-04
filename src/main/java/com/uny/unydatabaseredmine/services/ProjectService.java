package com.uny.unydatabaseredmine.services;

import com.uny.unydatabaseredmine.models.Project;

import com.uny.unydatabaseredmine.repos.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> filterProjectsByName(String name) {
        return projectRepository.findByName(name);
    }
    public void addProject(String name, String description, Date startDate, Date endDate) {
        projectRepository.addProject(name, description, startDate, endDate);
    }
}
