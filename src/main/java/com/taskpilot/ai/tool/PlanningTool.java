package com.taskpilot.ai.tool;

import java.util.Comparator;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

@Component
public class PlanningTool {

    private final TaskRepository taskRepository;

    public PlanningTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = """
    		Analyzes all pending tasks and recommends what the user should work on next.

    		Use this tool whenever the user asks for:

    		- planning
    		- recommendations
    		- organizing work
    		- today's work
    		- next task
    		- productivity planning
    		- scheduling
    		- work advice

    		Examples:

    		What should I work on?

    		Help me organize my work.

    		I'm overwhelmed.

    		I only have one hour today.

    		What should I do first?

    		Recommend my next task.

    		Always analyze the user's pending tasks before responding.
    		Never answer from memory.
    		""")
    public String planMyDay() {

        List<Task> tasks = taskRepository.findAll();

        List<Task> pendingTasks = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.PENDING)
                .sorted(
                        Comparator
                                .comparing((Task task) -> {
                                    if (task.getPriority() == TaskPriority.HIGH)
                                        return 1;
                                    if (task.getPriority() == TaskPriority.MEDIUM)
                                        return 2;
                                    return 3;
                                })
                                .thenComparing(
                                        Task::getDueDate,
                                        Comparator.nullsLast(Comparator.naturalOrder())
                                )
                )
                .toList();

        if (pendingTasks.isEmpty()) {
            return """
                    🎉 Great job!

                    You have no pending tasks.
                    Enjoy your day!
                    """;
        }

        StringBuilder plan = new StringBuilder();

        plan.append("## 🎯 Today's Recommended Tasks\n\n");

        int rank = 1;

        for (Task task : pendingTasks) {

            String medal = switch (rank) {
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> "📌";
            };

            plan.append(medal)
                .append(" ")
                .append(task.getTitle())
                .append("\n");

            plan.append("🔥 Priority: ")
                .append(task.getPriority())
                .append("\n");

            plan.append("📅 Due: ");

            if (task.getDueDate() != null) {
                plan.append(task.getDueDate().toLocalDate());
            } else {
                plan.append("No Due Date");
            }

            plan.append("\n");

            if (task.getPriority() == TaskPriority.HIGH) {
                plan.append("💡 Reason: High priority task that deserves immediate attention.\n");
            } else if (task.getPriority() == TaskPriority.MEDIUM) {
                plan.append("💡 Reason: Important task to complete after high-priority work.\n");
            } else {
                plan.append("💡 Reason: Lower priority task. Finish it after urgent work.\n");
            }

            plan.append("\n━━━━━━━━━━━━━━━━━━\n\n");

            rank++;

            if (rank > 5) {
                break;
            }
        }

        plan.append("⏱ **Recommendation**\n\n");
        plan.append("Focus on the first two tasks before starting anything new.");

        return plan.toString();
    }
}