package com.uny.unydatabaseredmine.controllers;

import com.uny.unydatabaseredmine.auth.config.RoleUtil;
import com.uny.unydatabaseredmine.models.Comment;
import com.uny.unydatabaseredmine.models.Severity;
import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskAssigneesService taskAssigneesService;

    @GetMapping("/tasks")
    public String listTasks(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "category", required = false) String category,
                            Model model, HttpSession session) {
        List<Task> tasks = taskService.getAllTasks();
        if (title != null) {
            if(title != ""){
                tasks = tasks.stream()
                        .filter(t -> t.getTitle().toLowerCase().contains(title.toLowerCase()))
                        .toList();
            }

        }
        if (category != null) {
            if(category != ""){
                tasks = tasks.stream()
                        .filter(t -> t.getCategory().toString().equalsIgnoreCase(category))
                        .toList();
            }

        }
        model.addAttribute("tasks", tasks);
        RoleUtil.extractAndSetUserRole(session);

        model.addAttribute("role", session.getAttribute("role"));
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
        assert task != null;
        List<Comment> comments = commentService.getCommentsByTaskId(task.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("employees", employeeService.getAllEmployees());
        Long employeeId = taskAssigneesService.getAssignee(task.getId());
        String employeeName;
        if (employeeId != null) {
            employeeName = employeeService.getEmployeeById(employeeId);
        }
        else
            employeeName = "";

        model.addAttribute("assignedEmployee", employeeName);
        return "task-details";
    }
    @GetMapping("/tasks/create")
    public String createTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create_task";
    }

    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute Task task) {
        task.setCompleted(false);
        task.setTotalTimeSpent(0);
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        model.addAttribute("projects", projectService.getAllProjects());

        return "edit_task";
    }

    @PostMapping("/tasks/edit/{id}")
    public String editTask(@ModelAttribute Task task, @PathVariable Long id) {
        task.setId(id);
        task.setCompleted(false);
        var tmTask = taskService.getTaskById(id);
        tmTask.setProjectId(task.getProjectId());
        taskService.updateTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
    @PostMapping("/tasks/{taskId}/add-comment")
    public String addCommentToTask(@PathVariable Long taskId, @RequestParam String commentText, RedirectAttributes redirectAttributes, HttpSession session) {
        // Assuming the employeeId is taken from the logged-in user (for example, from session)
        Long employeeId = RoleUtil.getUserId(session); // Method to fetch the logged-in user's ID
        Comment newComment = new Comment();
        newComment.setTaskId(taskId);
        newComment.setEmployeeId(employeeId);
        newComment.setCommentText(commentText);
        newComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        commentService.saveComment(newComment);

        redirectAttributes.addFlashAttribute("message", "Comment added successfully.");
        return "redirect:/tasks/" + taskId;  // Redirect back to the task details page
    }

    @PostMapping("/tasks/{taskId}/assign-employee")
    public String assignEmployeeToTask(@PathVariable Long taskId, @RequestParam Long employeeId, @RequestParam String severity) {
        taskAssigneesService.save(taskId, employeeId, Severity.valueOf(severity));
        return "redirect:/tasks/" + taskId;
    }


}
