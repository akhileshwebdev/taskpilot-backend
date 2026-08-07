package com.taskpilot.ai.tool;

import java.util.List;

import java.time.LocalDateTime;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.taskpilot.dto.TaskRequestDTO;
import com.taskpilot.dto.TaskResponseDTO;
import com.taskpilot.task.TaskPriority;
import com.taskpilot.task.TaskService;
import com.taskpilot.task.TaskStatus;

@Component
public class TaskTool {

    private final TaskService taskService;

    public TaskTool(TaskService taskService) {
        this.taskService = taskService;
    }

    
    @Tool(description = """
    		Updates an existing task.

    		Use whenever the user wants to:

    		- Change description.
    		- Update priority.
    		- Modify task details.

    		Examples:

    		- Update Docker description.
    		- Change Redis priority.
    		- Edit Kafka task.

    		Infer the user's intention naturally.
    		""")
    public TaskResponseDTO updateTask(
            String title,
            String description,
            String priority) {

        TaskPriority taskPriority = null;

        if (priority != null && !priority.isBlank()) {
            taskPriority = TaskPriority.valueOf(priority.toUpperCase());
        }

        return taskService.updateTaskByTitle(
                title,
                description,
                taskPriority
        );
    }
    @Tool(description = """
    		Deletes one or more tasks.

    		Use whenever the user no longer wants a task.

    		Examples:

    		- Delete Docker.
    		- Remove Redis.
    		- Forget Kafka.
    		- I don't need React anymore.
    		- Cancel Spring Boot.

    		Infer the user's intention naturally.
    		""")
    public String deleteTask(String title) {

        return taskService.deleteTaskByTitle(title);
    }
    @Tool(description = "Get all tasks for the logged-in user")
    public List<TaskResponseDTO> getAllTasks() {

        return taskService.getAllTasksForCurrentUser();
    }
    @Tool(description = """
    		Marks one or more tasks as completed.

    		Use whenever the user says:

    		- I finished Docker.
    		- Docker is done.
    		- I completed Kafka.
    		- Wrapped up Redis.
    		- Cross Docker off my list.
    		- Finished my Spring Boot course.

    		Infer the user's intent naturally.
    		""")
    public TaskResponseDTO completeTask(String title) {

        return taskService.completeTaskByTitle(title);
    }
    @Tool(description = """
    		Searches tasks.

    		Use whenever the user wants to:

    		- Show tasks
    		- Find tasks
    		- List tasks
    		- Search tasks
    		- Pending tasks
    		- Completed tasks
    		- High priority tasks
    		- Low priority tasks
    		- Overdue tasks

    		Examples:

    		Show my tasks.

    		Find Kafka.

    		List pending work.

    		What tasks do I have?

    		Show completed tasks.
    		""")
    public List<TaskResponseDTO> searchTasks(
            String status,
            String priority) {

        System.out.println("===== SEARCH TASK TOOL =====");

        return taskService.searchTasks(status, priority);
    }
    @Tool(description = """
    		Creates a new task.

    		Understand the user's intention naturally.

    		Examples:

    		- I need to learn Kafka tomorrow.
    		- Remind me to study Redis.
    		- Finish Docker this Friday.
    		- Add a task for React.
    		- I should complete Spring Boot.

    		Rules:

    		- Title is required.
    		- If description is missing, use the title.
    		- If priority is missing, use MEDIUM.
    		- If due date is missing, leave it empty.
    		- Convert natural language dates like
    		  tomorrow, Friday, next week
    		  into ISO-8601 format.

    		Never ask for missing fields unless the task title itself is unclear.
    		""")
    public TaskResponseDTO createTask(
            String title,
            String description,
            String priority,
            String dueDate) {

        TaskRequestDTO request = new TaskRequestDTO();

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title is required.");
        }

        request.setTitle(title.trim());
        if (description == null || description.isBlank()) {

            request.setDescription(title);

        } else {

            request.setDescription(description);

        }
        if (priority == null || priority.isBlank()) {

            request.setPriority(TaskPriority.MEDIUM);

        } else {

            request.setPriority(
                    TaskPriority.valueOf(priority.toUpperCase())
            );

        }
        request.setStatus(TaskStatus.PENDING);

        if (dueDate != null && !dueDate.isBlank()) {
            request.setDueDate(LocalDateTime.parse(dueDate));
        }

        return taskService.createTask(request);
    }
    @Tool(description = """
    		Plans the user's work.

    		Use whenever the user wants help deciding what to do next.

    		Examples:

    		What should I work on today?

    		Help me organize my work.

    		Plan my day.

    		Recommend my next task.

    		I'm overwhelmed.

    		I only have one hour today.

    		What should I do first?

    		Suggest today's work.
    		""")
    public List<TaskResponseDTO> getPlanningTasks() {

        System.out.println("===== PLANNER TOOL =====");

        return taskService.getPendingTasks();
    }
    @Tool(description = """
    		Returns all tasks.

    		Use whenever the AI needs to understand the user's complete task list
    		before making recommendations or answering questions.
    		""")
    		public List<TaskResponseDTO> getTasks() {
    		    return taskService.getAllTasksForCurrentUser();
    		}
    
}