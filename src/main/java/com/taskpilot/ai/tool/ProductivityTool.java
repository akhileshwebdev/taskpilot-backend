package com.taskpilot.ai.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

//@Component
public class ProductivityTool {

    private final TaskRepository taskRepository;

    public ProductivityTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = "Returns the user's productivity report and productivity score.")
    public String getProductivityReport() {

        List<Task> tasks = taskRepository.findAll();

        long total = tasks.size();

        if (total == 0) {
            return """
                    📈 Productivity Report

                    You don't have any tasks yet.

                    Start by creating your first task!
                    """;
        }

        long completed = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();

        long pending = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .count();

        long highPriorityPending = tasks.stream()
                .filter(task ->
                        task.getStatus() == TaskStatus.PENDING &&
                        task.getPriority() == TaskPriority.HIGH)
                .count();

        int productivityScore = (int) ((completed * 100) / total);

        String recommendation;

        if (productivityScore >= 80) {
            recommendation = "🎉 Excellent work! Keep maintaining your productivity.";
        } else if (productivityScore >= 50) {
            recommendation = "👍 You're making good progress. Try completing a few more high-priority tasks.";
        } else {
            recommendation = "⚠️ Focus on completing your high-priority pending tasks to improve your productivity.";
        }

        return """
                📈 Productivity Report

                🎯 Productivity Score: %d%%

                📋 Total Tasks: %d
                ✅ Completed: %d
                ⏳ Pending: %d
                🔥 High Priority Pending: %d

                💡 Recommendation:
                %s
                """
                .formatted(
                        productivityScore,
                        total,
                        completed,
                        pending,
                        highPriorityPending,
                        recommendation
                );
    }
}