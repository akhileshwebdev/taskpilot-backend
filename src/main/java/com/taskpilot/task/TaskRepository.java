package com.taskpilot.task;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.taskpilot.user.User;

public interface TaskRepository extends JpaRepository<Task, Long>{
	
	List<Task> findByStatus(TaskStatus status);

	List<Task> findByPriority(TaskPriority priority);

	List<Task> findByStatusAndPriority(
	        TaskStatus status,
	        TaskPriority priority);
	@Query("SELECT t FROM Task t WHERE t.status = :status")
	List<Task> getTasksByStatusJPQL(@Param("status") TaskStatus status);
	
	@Query("SELECT t FROM Task t WHERE t.title LIKE %:title%")
	
	List<Task> searchByTitle(@Param("title") String title);
	@Query(
		    value = "SELECT * FROM task WHERE priority = :priority",
		    nativeQuery = true
		)
		List<Task> getTasksByPriorityNative(
		        @Param("priority") String priority);
	Page<Task> findByUser(User user, Pageable pageable);
	
	Optional<Task> findByTitleAndUser(String title, User user);
	List<Task> findByUser(User user);
	
	List<Task> findByUserAndStatus(User user, TaskStatus status);
	
	List<Task> findByUserAndPriorityAndStatus(
	        User user,
	        TaskPriority priority,
	        TaskStatus status);

	List<Task> findByUserAndPriority(User user, TaskPriority priority);

	List<Task> findByUserAndStatusAndPriority(
	        User user,
	        TaskStatus status,
	        TaskPriority priority);
	@Query("""
			SELECT t
			FROM Task t
			WHERE
			t.user = :user
			AND
			(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))
			AND
			(:status IS NULL OR t.status = :status)
			AND
			(:priority IS NULL OR t.priority = :priority)
			""")
	List<Task> filterTasks(
	        @Param("user") User user,
	        @Param("title") String title,
	        @Param("status") TaskStatus status,
	        @Param("priority") TaskPriority priority);
	Optional<Task> findByTitleIgnoreCase(String title);
	List<Task> findByTitleContainingIgnoreCase(String title);
	

}
