package com.taskpilot.ai.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

//@Component
public class WeeklyReportTool {

    private final TaskRepository taskRepository;

    public WeeklyReportTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = "Returns the user's weekly task report.")
    public String getWeeklyReport() {

        List<Task> tasks = taskRepository.findAll();

        long total = tasks.size();

        if (total == 0) {
            return """
                    📅 Weekly Report

                    No tasks available this week.
                    """;
        }

        long completed = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();

        long pending = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .count();

        long high = tasks.stream()
                .filter(task -> task.getPriority() == TaskPriority.HIGH)
                .count();

        long medium = tasks.stream()
                .filter(task -> task.getPriority() == TaskPriority.MEDIUM)
                .count();

        long low = tasks.stream()
                .filter(task -> task.getPriority() == TaskPriority.LOW)
                .count();

        int completionRate = (int) ((completed * 100) / total);

        String insight;

        if (completionRate >= 80) {
            insight = "🎉 Excellent week! Keep up the great work.";
        } else if (completionRate >= 50) {
            insight = "👍 Good progress. Try completing a few more pending tasks.";
        } else {
            insight = "⚠️ You have many pending tasks. Focus on finishing high-priority work first.";
        }

        return String.format(
                "📅 Weekly Report\n" +
                "📋 Total Tasks: %d\n" +
                "✅ Completed: %d\n" +
                "⏳ Pending: %d\n\n" +
                "🔥 Priority Breakdown\n" +
                "• High: %d\n" +
                "• Medium: %d\n" +
                "• Low: %d\n\n" +
                "📈 Completion Rate: %d%%\n\n" +
                "💡 Weekly Insight\n%s",
                total,
                completed,
                pending,
                high,
                medium,
                low,
                completionRate,
                insight
        );
    }
}