package com.uny.unydatabaseredmine.controllers;

import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.services.TaskService;
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
@PreAuthorize("hasRole('USER')")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public String listTasks(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "category", required = false) String category,
                            Model model) {
        List<Task> tasks = taskService.getAllTasks();
        if (title != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }
        if (category != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getCategory().toString().equalsIgnoreCase(category))
                    .toList();
        }
        model.addAttribute("tasks", tasks);
        return "tasks";
    }


    @GetMapping("/tasks/{id}")
    public String taskDetails(@PathVariable Long id, Model model) {
        Task task = taskService.getAllTasks()
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("task", task);
        return "task-details";
    }
    @GetMapping("/tasks/create")
    public String createTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create_task";
    }

    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "edit_task";
    }

    @PostMapping("/tasks/edit")
    public String editTask(@ModelAttribute Task task) {
        taskService.updateTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }


}
