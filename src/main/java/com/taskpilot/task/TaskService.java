package com.taskpilot.task;

import java.util.ArrayList;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskpilot.dto.TaskRequestDTO;
import com.taskpilot.dto.TaskResponseDTO;
import com.taskpilot.dto.UserSummaryDTO;
import com.taskpilot.exception.TaskNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.taskpilot.user.User;
import com.taskpilot.user.UserService;


@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	@Autowired
	private UserService userService;
	
	public TaskResponseDTO createTask(TaskRequestDTO request) {

	    Task task = new Task();
	    task.setTitle(request.getTitle());
	    task.setDescription(request.getDescription());
	    task.setStatus(request.getStatus());
	    task.setPriority(request.getPriority());
	    task.setDueDate(request.getDueDate());

	    User currentUser = userService.getCurrentUser();
	    task.setUser(currentUser);

	    // Check if a task with the same title already exists
	    Optional<Task> existingTask =
	            taskRepository.findByTitleAndUser(request.getTitle(), currentUser);

	    if (existingTask.isPresent()) {
	        throw new RuntimeException("A task with this title already exists.");
	    }

	    // Save the task
	    Task savedTask = taskRepository.save(task);

	    return mapToResponse(savedTask);
	}
	
	public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {

	    User currentUser = userService.getCurrentUser();

	    System.out.println("========== DEBUG ==========");
	    System.out.println("Current User ID: " + currentUser.getId());
	    System.out.println("Current User Email: " + currentUser.getEmail());

	    Page<Task> page = taskRepository.findByUser(currentUser, pageable);

	    System.out.println("Tasks Found: " + page.getTotalElements());
	    System.out.println("===========================");

	    return page.map(this::mapToResponse);
	}
	
	
	 public TaskResponseDTO getTaskById(Long id) {

	        Task task = findTaskById(id);

	        return mapToResponse(task);
	    }
	 public TaskResponseDTO updateTask(Long id, TaskRequestDTO request) {

		    
		    Task existingTask = findTaskById(id);

		    
		    existingTask.setTitle(request.getTitle());
		    existingTask.setDescription(request.getDescription());
		    existingTask.setStatus(request.getStatus());
		    existingTask.setPriority(request.getPriority());
		    existingTask.setDueDate(request.getDueDate());
		    
		    User currentUser = userService.getCurrentUser();
		    existingTask.setUser(currentUser);

		   
		    Task updatedTask = taskRepository.save(existingTask);

		    
		    return mapToResponse(updatedTask);
		}
	 public void deleteTask(Long id) {

	        findTaskById(id);

	        taskRepository.deleteById(id);
	    }
	 private Task findTaskById(Long id) {

		    Task task = taskRepository.findById(id)
		            .orElseThrow(() ->
		                    new TaskNotFoundException("Task not found " + id));

		    User currentUser = userService.getCurrentUser();

		    if (!task.getUser().getId().equals(currentUser.getId())) {
		        throw new RuntimeException("Access Denied");
		    }

		    return task;
		}
	 
	private TaskResponseDTO mapToResponse(Task task) {

	    TaskResponseDTO response = new TaskResponseDTO();

	    response.setId(task.getId());
	    response.setTitle(task.getTitle());
	    response.setDescription(task.getDescription());
	    response.setStatus(task.getStatus());
	    response.setPriority(task.getPriority());
	    response.setDueDate(task.getDueDate());
	    response.setCreatedAt(task.getCreatedAt());
	    response.setUpdatedAt(task.getUpdatedAt());
	    UserSummaryDTO userSummary = new UserSummaryDTO(
	            task.getUser().getId(),
	            task.getUser().getName(),
	            task.getUser().getEmail()
	    );

	    response.setUser(userSummary);

	    return response;
	}
	public List<TaskResponseDTO> getTasksByStatus(TaskStatus status) {

		List<Task> tasks = taskRepository.getTasksByStatusJPQL(status);

	    List<TaskResponseDTO> responseList = new ArrayList<>();

	    for (Task task : tasks) {
	        responseList.add(mapToResponse(task));
	    }

	    return responseList;
	}
	public List<TaskResponseDTO> getTasksByPriority(TaskPriority priority) {

	    List<Task> tasks = taskRepository.findByPriority(priority);

	    List<TaskResponseDTO> responseList = new ArrayList<>();

	    for (Task task : tasks) {
	        responseList.add(mapToResponse(task));
	    }

	    return responseList;
	}
	public List<TaskResponseDTO> getTasksByStatusAndPriority(
	        TaskStatus status,
	        TaskPriority priority) {

	    List<Task> tasks = taskRepository.findByStatusAndPriority(status, priority);

	    List<TaskResponseDTO> responseList = new ArrayList<>();

	    for (Task task : tasks) {
	        responseList.add(mapToResponse(task));
	    }

	    return responseList;
	}
	public List<TaskResponseDTO> searchTasksByTitle(String title) {

	    List<Task> tasks = taskRepository.searchByTitle(title);

	    List<TaskResponseDTO> responseList = new ArrayList<>();

	    for (Task task : tasks) {
	        responseList.add(mapToResponse(task));
	    }

	    return responseList;
	}
	public List<TaskResponseDTO> getTasksByPriorityNative(String priority) {

	    List<Task> tasks = taskRepository.getTasksByPriorityNative(priority);

	    List<TaskResponseDTO> responseList = new ArrayList<>();

	    for (Task task : tasks) {
	        responseList.add(mapToResponse(task));
	    }

	    return responseList;
	}
	public TaskResponseDTO updateTaskByTitle(
	        String title,
	        String description,
	        TaskPriority priority) {

	    User currentUser = userService.getCurrentUser();

	    Task task = taskRepository
	            .findByTitleAndUser(title, currentUser)
	            .orElseThrow(() ->
	                    new TaskNotFoundException("Task not found: " + title));

	    if (description != null && !description.isBlank()) {
	        task.setDescription(description);
	    }

	    if (priority != null) {
	        task.setPriority(priority);
	    }

	    Task updatedTask = taskRepository.save(task);

	    return mapToResponse(updatedTask);
	}
	public String deleteTaskByTitle(String title) {

	    User currentUser = userService.getCurrentUser();

	    Task task = taskRepository
	            .findByTitleAndUser(title, currentUser)
	            .orElseThrow(() ->
	                    new TaskNotFoundException("Task not found: " + title));

	    taskRepository.delete(task);

	    return "Task \"" + title + "\" deleted successfully.";
	}
	public List<TaskResponseDTO> getAllTasksForCurrentUser() {

	    User currentUser = userService.getCurrentUser();

	    List<Task> tasks = taskRepository.findByUser(currentUser);

	    return tasks.stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	public TaskResponseDTO completeTaskByTitle(String title) {

	    User currentUser = userService.getCurrentUser();

	    Task task = taskRepository
	            .findByTitleAndUser(title, currentUser)
	            .orElseThrow(() ->
	                    new TaskNotFoundException("Task not found: " + title));

	    task.setStatus(TaskStatus.COMPLETED);

	    Task updatedTask = taskRepository.save(task);

	    return mapToResponse(updatedTask);
	}
	public List<TaskResponseDTO> getPendingTasks() {

	    User currentUser = userService.getCurrentUser();

	    List<Task> tasks =
	            taskRepository.findByUserAndStatus(currentUser, TaskStatus.PENDING);

	    return tasks.stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	public List<TaskResponseDTO> getHighPriorityTasks() {

	    User currentUser = userService.getCurrentUser();

	    List<Task> tasks =
	            taskRepository.findByUserAndPriorityAndStatus(
	                    currentUser,
	                    TaskPriority.HIGH,
	                    TaskStatus.PENDING);

	    return tasks.stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	public List<TaskResponseDTO> searchTasks(
	        String status,
	        String priority) {

	    User currentUser = userService.getCurrentUser();

	    TaskStatus taskStatus = null;
	    TaskPriority taskPriority = null;

	    if (status != null && !status.isBlank()) {
	        taskStatus = TaskStatus.valueOf(status.toUpperCase());
	    }

	    if (priority != null && !priority.isBlank()) {
	        taskPriority = TaskPriority.valueOf(priority.toUpperCase());
	    }

	    List<Task> tasks;

	    if (taskStatus != null && taskPriority != null) {
	        tasks = taskRepository.findByUserAndStatusAndPriority(
	                currentUser,
	                taskStatus,
	                taskPriority);
	    }
	    else if (taskStatus != null) {
	        tasks = taskRepository.findByUserAndStatus(
	                currentUser,
	                taskStatus);
	    }
	    else if (taskPriority != null) {
	        tasks = taskRepository.findByUserAndPriority(
	                currentUser,
	                taskPriority);
	    }
	    else {
	        tasks = taskRepository.findByUser(currentUser);
	    }

	    return tasks.stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	public List<TaskResponseDTO> filterTasks(
	        String title,
	        TaskStatus status,
	        TaskPriority priority) {

	    User currentUser = userService.getCurrentUser();

	    return taskRepository
	            .filterTasks(currentUser, title, status, priority)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}

}
