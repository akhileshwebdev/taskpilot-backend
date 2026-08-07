package com.taskpilot.task;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taskpilot.dto.TaskRequestDTO;
import com.taskpilot.dto.TaskResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	 private final TaskService taskService;
	 
	 public TaskController(TaskService taskService) {
	     this.taskService = taskService;
	 }
	 
	 @PostMapping
	 public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO request) {
	     return taskService.createTask(request);
	 }
	 
	 @GetMapping
	 public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
	     return taskService.getAllTasks(pageable);
	 }
	 
	 
	 @PutMapping("/{id}")
	 public TaskResponseDTO updateTask(@PathVariable Long id,
			 @Valid @RequestBody TaskRequestDTO request) {
	     return taskService.updateTask(id, request);
	 }
	 
	 @DeleteMapping("/{id}")
	 public void deleteTask(@PathVariable Long id) {
	     taskService.deleteTask(id);
	 }
	 
	 @GetMapping("/{id}")
	 public TaskResponseDTO getTaskById(@PathVariable Long id) {
	     return taskService.getTaskById(id);
	 }
	 @GetMapping("/status/{status}")
	 public List<TaskResponseDTO> getTasksByStatus(@PathVariable TaskStatus status) {
	     return taskService.getTasksByStatus(status);
	 }
	 @GetMapping("/priority/{priority}")
	 public List<TaskResponseDTO> getTasksByPriority(
	         @PathVariable TaskPriority priority) {

	     return taskService.getTasksByPriority(priority);
	 }
	 @GetMapping("/status/{status}/priority/{priority}")
	 public List<TaskResponseDTO> getTasksByStatusAndPriority(
	         @PathVariable TaskStatus status,
	         @PathVariable TaskPriority priority) {

	     return taskService.getTasksByStatusAndPriority(status, priority);
	 }
	 @GetMapping("/search")
	 public List<TaskResponseDTO> searchTasksByTitle(
	         @RequestParam String title) {

	     return taskService.searchTasksByTitle(title);
	 }
	 @GetMapping("/native/priority/{priority}")
	 public List<TaskResponseDTO> getTasksByPriorityNative(
	         @PathVariable String priority) {

	     return taskService.getTasksByPriorityNative(priority);
	 }
	 @GetMapping("/filter")
	 public List<TaskResponseDTO> filterTasks(

	         @RequestParam(required = false) String title,

	         @RequestParam(required = false) TaskStatus status,

	         @RequestParam(required = false) TaskPriority priority) {

	     return taskService.filterTasks(
	             title,
	             status,
	             priority);
	 }

}
