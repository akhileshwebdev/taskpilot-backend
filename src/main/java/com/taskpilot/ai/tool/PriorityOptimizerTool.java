package com.taskpilot.ai.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

@Component
public class PriorityOptimizerTool {

    private final TaskRepository taskRepository;

    public PriorityOptimizerTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = "Analyzes pending tasks and recommends better priorities.")
    public String optimizePriorities() {

        List<Task> tasks = taskRepository.findAll();

        List<Task> pendingTasks = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .toList();

        if (pendingTasks.isEmpty()) {
            return """
                    🎉 Excellent!

                    You have no pending tasks.
                    """;
        }

        StringBuilder report = new StringBuilder();

        report.append("## 🚀 Priority Optimization\n\n");

        long highCount = pendingTasks.stream()
                .filter(t -> t.getPriority() == TaskPriority.HIGH)
                .count();

        if (highCount > 5) {
            report.append("⚠️ You currently have ")
                  .append(highCount)
                  .append(" HIGH priority tasks.\n");
            report.append("Too many HIGH priority tasks reduce focus.\n\n");
        }

        report.append("### 🔥 HIGH Priority\n\n");

        pendingTasks.stream()
                .filter(t -> t.getPriority() == TaskPriority.HIGH)
                .forEach(task -> {
                    report.append("• ")
                          .append(task.getTitle())
                          .append("\n");

                    if (task.getDueDate() != null) {
                        report.append("  📅 Due: ")
                              .append(task.getDueDate().toLocalDate())
                              .append("\n");
                    }

                    report.append("  💡 Keep this task HIGH priority.\n\n");
                });

        report.append("### 🟡 MEDIUM Priority\n\n");

        pendingTasks.stream()
                .filter(t -> t.getPriority() == TaskPriority.MEDIUM)
                .forEach(task -> {
                    report.append("• ")
                          .append(task.getTitle())
                          .append("\n");
                    report.append("  💡 Complete after HIGH priority tasks.\n\n");
                });

        report.append("### 🟢 LOW Priority\n\n");

        pendingTasks.stream()
                .filter(t -> t.getPriority() == TaskPriority.LOW)
                .forEach(task -> {
                    report.append("• ")
                          .append(task.getTitle())
                          .append("\n");
                    report.append("  💡 Schedule when urgent work is finished.\n\n");
                });

        report.append("## ✅ Recommendation\n\n");

        report.append("Keep only the most important 3–5 tasks as HIGH priority.\n");
        report.append("Move less urgent work to MEDIUM or LOW priority for better focus.");

        return report.toString();
    }
}