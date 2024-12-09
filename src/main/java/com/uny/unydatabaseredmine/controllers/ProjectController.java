package com.uny.unydatabaseredmine.controllers;

import com.uny.unydatabaseredmine.auth.config.RoleUtil;
import com.uny.unydatabaseredmine.models.Project;
import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.services.ProjectService;
import com.uny.unydatabaseredmine.services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/projects")
    public String listProjects(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               Model model, HttpSession session) {
        List<Project> projects = projectService.getAllProjects();
        if (name != null) {
            projects = projects.stream()
                    .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }
        if (startDate != null) {
            projects = projects.stream()
                    .filter(p -> p.getStartDate().toString().startsWith(startDate))
                    .toList();
        }
        model.addAttribute("projects", projects);
        RoleUtil.extractAndSetUserRole(session);
        model.addAttribute("role", session.getAttribute("role"));
        return "projects";
    }

    @GetMapping("/projects/create")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "create_project";
    }

    @PostMapping("/projects/create")
    public String createProject(@ModelAttribute Project project) {
        projectService.createProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/projects/edit/{id}")
    public String editProjectForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "edit_project";
    }

    @PostMapping("/projects/edit")
    public String editProject(@ModelAttribute Project project) {
        projectService.updateProject(project);
        return "redirect:/projects";
    }

    @PostMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }


    @GetMapping("/projects/{id}")
    public String projectDetails(@PathVariable Long id, Model model) {
        Project project = projectService.getAllProjects()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (project != null) {
            List<Task> tasks = taskService.filterTasksByProject(id);
            model.addAttribute("project", project);
            model.addAttribute("tasks", tasks);
        }
        return "project-details";
    }
}
