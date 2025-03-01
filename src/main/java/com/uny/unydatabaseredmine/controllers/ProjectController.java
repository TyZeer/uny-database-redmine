package com.uny.unydatabaseredmine.controllers;

import com.uny.unydatabaseredmine.auth.config.RoleUtil;
import com.uny.unydatabaseredmine.models.Project;
import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.services.ProjectService;
import com.uny.unydatabaseredmine.services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Controller
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;
    @GetMapping("/projects/api")
    public ResponseEntity<?> listProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/projects")
    public String listProjects(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               Model model, HttpSession session) {
        // Получить все проекты
        List<Project> projects = projectService.getAllProjects();

        // Фильтрация по имени
        if (name != null) {
            projects = projects.stream()
                    .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }

        // Фильтрация по дате начала
        if (startDate != null) {
            projects = projects.stream()
                    .filter(p -> p.getStartDate().toString().startsWith(startDate))
                    .toList();
        }

        // Путь к папке с фотографиями
        String photoDirectory = "C:\\Users\\Dmitriy Pavlov\\OneDrive\\Рабочий стол\\uny-database-redmine\\src\\main\\java\\com\\uny\\unydatabaseredmine\\upload";

        // Добавление Base64 изображений к проектам
        for (Project project : projects) {
            File folder = new File(photoDirectory);
            if (folder.exists() && folder.isDirectory()) {
                File[] matchingFiles = folder.listFiles((dir, name1) -> name1.startsWith("projects" + project.getId() + "_"));
                if (matchingFiles != null && matchingFiles.length > 0) {
                    try {
                        byte[] fileContent = Files.readAllBytes(matchingFiles[0].toPath());
                        String base64Image = Base64.getEncoder().encodeToString(fileContent);
                        project.setPhotoPath("data:image/jpeg;base64," + base64Image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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

    @PostMapping("/projects/edit/{id}")
    public String editProject(@ModelAttribute Project updatedProject,
                              @PathVariable Long id,
                              @RequestParam("photo") MultipartFile photo) throws IOException {
        updatedProject.setId(id);
        Project existingProject = projectService.getProjectById(updatedProject.getId());

        // Обновляем остальные поля
        existingProject.setName(updatedProject.getName() != null ? updatedProject.getName() : existingProject.getName());
        existingProject.setDescription(updatedProject.getDescription() != null ? updatedProject.getDescription() : existingProject.getDescription());
        existingProject.setStartDate(updatedProject.getStartDate() != null ? updatedProject.getStartDate() : existingProject.getStartDate());
        existingProject.setEndDate(updatedProject.getEndDate() != null ? updatedProject.getEndDate() : existingProject.getEndDate());

        // Обработка фотографии
        if (!photo.isEmpty()) {
            String uploadDir = "C:\\Users\\Dmitriy Pavlov\\OneDrive\\Рабочий стол\\uny-database-redmine\\src\\main\\java\\com\\uny\\unydatabaseredmine\\upload\\projects";
            String photoPath = uploadDir + id + "_" + photo.getOriginalFilename();
            File uploadFile = new File(photoPath);
            uploadFile.getParentFile().mkdirs(); // Создаем директории, если их нет
            photo.transferTo(uploadFile);       // Сохраняем файл
            existingProject.setPhotoPath("/" + photoPath);

        }

        projectService.updateProject(existingProject);
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

    @GetMapping("/projects/{id}/assign-task")
    public String assignTaskToProjectForm(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        List<Task> tasks = taskService.getAllTasks();
        List<Task> suitableTasks = tasks.stream().filter(task -> !Objects.equals(task.getProjectId(), project.getId())).toList();
        model.addAttribute("project", project);
        model.addAttribute("tasks", suitableTasks);
        return "assign_task_to_project";
    }

    @PostMapping("/projects/{projectId}/assign-task")
    public String assignTaskToProject(@PathVariable Long projectId, Long task, RedirectAttributes redirectAttributes) {
        Project project = projectService.getProjectById(projectId);
        Task taskCreated = taskService.getTaskById(task);

        if (project != null && task != null) {
            taskCreated.setProjectId(projectId);
            taskService.assignTaskToProject(taskCreated.getProjectId(), taskCreated.getId());
            redirectAttributes.addFlashAttribute("message", "Task successfully assigned to the project.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to assign task.");
        }

        return "redirect:/projects/" + projectId;
    }
}
