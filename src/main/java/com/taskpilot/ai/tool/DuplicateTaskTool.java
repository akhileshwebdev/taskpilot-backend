package com.taskpilot.ai.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

//@Component
public class DuplicateTaskTool {

    private final TaskRepository taskRepository;

    public DuplicateTaskTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = "Checks whether a similar task already exists.")
    public String checkDuplicateTask(String title) {

        List<Task> tasks = taskRepository.findAll();

        List<Task> similarTasks = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.COMPLETED)
                .filter(task ->
                        task.getTitle().toLowerCase()
                                .contains(title.toLowerCase())
                        ||
                        title.toLowerCase()
                                .contains(task.getTitle().toLowerCase()))
                .toList();

        if (similarTasks.isEmpty()) {
            return "✅ No similar task found.";
        }

        StringBuilder result = new StringBuilder();

        result.append("⚠️ Similar tasks found:\n\n");

        for (Task task : similarTasks) {
            result.append("• ")
                  .append(task.getTitle())
                  .append("\n");
        }

        result.append("\nCreating another similar task may create duplicates.");

        return result.toString();
    }
}