package com.taskpilot.ai.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.task.Task;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskRepository;
import com.taskpilot.task.TaskStatus;

@Component
public class TaskEditTool {

    private final TaskRepository taskRepository;
    private Optional<Task> findTask(String title) {

        if (title == null || title.isBlank()) {
            return Optional.empty();
        }

        List<Task> tasks =
                taskRepository.findByTitleContainingIgnoreCase(title.trim());

        if (tasks.isEmpty()) {
            return Optional.empty();
        }

        tasks.sort((a, b) ->
                Integer.compare(a.getTitle().length(), b.getTitle().length()));

        return Optional.of(tasks.get(0));
    }

    public TaskEditTool(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Tool(description = """
    		Rename a task.

    		Examples:
    		- Rename Docker to Docker Advanced.
    		- Change Kafka to Apache Kafka.
    		- Rename React Project.

    		Infer the old and new task names naturally.
    		""")
    public String renameTask(String oldTitle, String newTitle) {

    	Optional<Task> optionalTask = findTask(oldTitle);

        if (optionalTask.isEmpty()) {
            return "❌ Task '" + oldTitle + "' was not found.";
        }

        Task task = optionalTask.get();

        task.setTitle(newTitle);

        taskRepository.save(task);

        return "Renamed task '" + oldTitle + "' to '" + newTitle + "'.";
    }
    @Tool(description = """
    		Complete a task.

    		Examples:
    		- I finished Docker.
    		- Docker is done.
    		- Completed Kafka.
    		- Finished React.

    		Infer the task title naturally.
    		""")
    public String markTaskCompleted(String title) {

    	Optional<Task> optionalTask = findTask(title);

        if (optionalTask.isEmpty()) {
            return "❌ Task '" + title + "' was not found.";
        }

        Task task = optionalTask.get();

        task.setStatus(TaskStatus.COMPLETED);

        taskRepository.save(task);

        return "Completed task: " + task.getTitle();
    }
    @Tool(description = """
    		Change task priority.

    		Examples:
    		- Docker is urgent.
    		- React can wait.
    		- Kafka is important.
    		- Make Redis HIGH priority.
    		- Lower Spring Boot priority.

    		Infer HIGH, MEDIUM or LOW naturally.
    		""")
    public String changeTaskPriority(String title, String priority) {

    	Optional<Task> optionalTask = findTask(title);

        if (optionalTask.isEmpty()) {
            return "❌ Task '" + title + "' was not found.";
        }

        Task task = optionalTask.get();

        TaskPriority newPriority;

        try {
            newPriority = TaskPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "❌ Invalid priority. Use HIGH, MEDIUM or LOW.";
        }

        task.setPriority(newPriority);

        taskRepository.save(task);

        return "Updated priority of '" + task.getTitle() + "' to " + newPriority + ".";
    }
    @Tool(description = """
    		Update a task due date.

    		Examples:
    		- Move Docker to tomorrow.
    		- Reschedule Kafka.
    		- Postpone React until next week.
    		- Set Redis due Friday.

    		Understand natural language dates.
    		""")
    public String changeTaskDueDate(String title, String dueDate) {

    	Optional<Task> optionalTask = findTask(title);

    	if (optionalTask.isEmpty()) {
    	    return "❌ Task '" + title + "' was not found.";
    	}

    	Task task = optionalTask.get();

        LocalDateTime newDueDate;

        switch (dueDate.toLowerCase()) {

            case "today" ->
                newDueDate = LocalDate.now().atTime(23, 59);

            case "tomorrow" ->
                newDueDate = LocalDate.now().plusDays(1).atTime(23, 59);

            default -> {
                try {
                    newDueDate = LocalDate.parse(dueDate).atTime(23, 59);
                } catch (Exception e) {
                    return "❌ Invalid date. Use YYYY-MM-DD, today or tomorrow.";
                }
            }
        }

        task.setDueDate(newDueDate);

        taskRepository.save(task);

        return "Updated due date of '" + task.getTitle() + "' to " + newDueDate.toLocalDate() + ".";
    }	
    @Tool(description = """
    		Delete a task.

    		Examples:
    		- Delete Docker.
    		- Remove Kafka.
    		- Forget Redis.
    		- I don't need React anymore.
    		- Cancel Spring Boot.

    		Infer the task title naturally.
    		""")
    public String deleteTaskByTitle(String title) {

        Optional<Task> optionalTask = findTask(title);

        if (optionalTask.isEmpty()) {
            return "❌ Task '" + title + "' was not found.";
        }

        Task task = optionalTask.get();

        taskRepository.delete(task);

        return "Deleted task: " + task.getTitle();
    }
}