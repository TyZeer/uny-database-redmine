package com.uny.unydatabaseredmine.services;

import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

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

    public Task getTaskById(Long id) {
       return taskRepository.findById(id);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);

    }

    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }
}
