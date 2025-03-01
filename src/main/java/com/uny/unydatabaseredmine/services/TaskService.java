package com.uny.unydatabaseredmine.services;

import com.uny.unydatabaseredmine.auth.models.Employee;
import com.uny.unydatabaseredmine.auth.repos.EmployeeRepository;
import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TaskAssigneesService taskAssigneesService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> filterTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> filterTasksByCategory(String category) {
        return taskRepository.findByCategory(category);
    }

    public void addTask(Long projectId, String title, String category, Date dueDate, int totalTimeSpent, int isCompleted) {
        boolean isCmp;
        if (isCompleted == 0)
            isCmp = true;
        else
            isCmp = false;
        taskRepository.addTask(projectId, title, category, dueDate, totalTimeSpent, isCmp);
    }
    public void createTask(Task task) {
        taskRepository.addTask(
                task.getProjectId(),
                task.getTitle(),
                task.getCategory().name(),
                task.getDueDate(),
                task.getTotalTimeSpent(),
                task.isCompleted()
        );
    }
    public Map<String, List<Task>> getTasksForAllUsers() {
        List<Employee> employeesTg = employeeRepository.findAllWithTgName();
        Map<String, List<Task>> tasksForAllUsers = new HashMap<>();
        for (Employee employee : employeesTg) {
            List<Task> tasks = new ArrayList<>();
            List<Long> ids = taskAssigneesService.getAllTasksIdByEmployee(employee.getId());
            for (Long id : ids) {
                tasks.add(getTaskById(id));
            }
            tasksForAllUsers.put(employee.getTgName(), tasks);
        }
        return tasksForAllUsers;
    }

    public Task getTaskById(Long id) {
       return taskRepository.findById(id);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);

    }
    public void assignTaskToProject(Long projectId, Long taskId) {
        taskRepository.assignTaskToProject(projectId, taskId);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }
}
