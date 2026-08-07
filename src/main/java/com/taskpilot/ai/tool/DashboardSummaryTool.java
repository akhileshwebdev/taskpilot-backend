package com.taskpilot.ai.tool;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskStatus;

@Component
public class DashboardSummaryTool {

    private final TaskRepository taskRepository;

    public DashboardSummaryTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    

    @Tool(description = "Returns the current dashboard productivity summary.")
    public String getDashboardSummary() {

        System.out.println("Step 1");

        List<Task> tasks = taskRepository.findAll();

        System.out.println("Step 2");

        long total = tasks.size();

        System.out.println("Step 3");

        long completed = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();

        System.out.println("Step 4");

        long pending = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .count();

        System.out.println("Step 5");

        long highPriority = tasks.stream()
                .filter(task -> task.getPriority() == TaskPriority.HIGH)
                .count();

        System.out.println("Step 6");

        long dueToday = tasks.stream()
                .filter(task ->
                        task.getDueDate() != null &&
                        task.getDueDate().toLocalDate().equals(LocalDate.now()))
                .count();

        System.out.println("Step 7");

        return """
        		📊 Dashboard Summary

        		📋 Total Tasks: %d
        		✅ Completed: %d
        		⏳ Pending: %d
        		🔥 High Priority: %d
        		📅 Due Today: %d
        		"""
        		.formatted(
        		        total,
        		        completed,
        		        pending,
        		        highPriority,
        		        dueToday
        		);
    }
}