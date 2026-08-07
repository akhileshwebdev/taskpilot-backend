package com.taskpilot.ai.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

//@Component
public class InsightsTool {

    private final TaskRepository taskRepository;

    public InsightsTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = "Analyzes the user's productivity and provides personalized insights.")
    public String getProductivityInsights() {

        List<Task> tasks = taskRepository.findAll();

        if (tasks.isEmpty()) {
            return "You don't have any tasks yet. Create a few tasks first so I can analyze your productivity.";
        }

        long total = tasks.size();

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

        long withoutDueDate = tasks.stream()
                .filter(task -> task.getDueDate() == null)
                .count();

        int completionRate = (int) ((completed * 100) / total);

        StringBuilder insights = new StringBuilder();

        insights.append("🧠 Productivity Insights\n\n");
        insights.append("Based on your current tasks, here's what I noticed:\n\n");

        insights.append("🔴 You have ")
                .append(pending)
                .append(" pending tasks.\n");

        insights.append("🔥 ")
                .append(highPriorityPending)
                .append(" of them are HIGH priority.\n");

        insights.append("📉 Your completion rate is ")
                .append(completionRate)
                .append("% (")
                .append(completed)
                .append(" of ")
                .append(total)
                .append(" tasks completed).\n");

        insights.append("📅 ")
                .append(withoutDueDate)
                .append(" tasks don't have due dates.\n\n");

        insights.append("💡 Recommendations\n\n");

        if (highPriorityPending > 5) {
            insights.append("• Reduce the number of HIGH priority tasks.\n");
        }

        if (withoutDueDate > 0) {
            insights.append("• Add due dates to tasks without deadlines.\n");
        }

        if (pending > completed) {
            insights.append("• Complete existing tasks before creating new ones.\n");
        }

        insights.append("• Finish your due-today task first.\n");
        insights.append("• Focus on one task at a time.\n\n");

        insights.append("🎯 Today's Goal\n");
        insights.append("Complete at least 2 high-priority tasks to improve your productivity.");

        return insights.toString();
    }
}