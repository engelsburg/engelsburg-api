package io.github.paexception.engelsburg.api.controller.reserved;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.paexception.engelsburg.api.controller.userdata.UserDataHandler;
import io.github.paexception.engelsburg.api.database.model.TaskModel;
import io.github.paexception.engelsburg.api.database.repository.TaskRepository;
import io.github.paexception.engelsburg.api.endpoint.dto.TaskDTO;
import io.github.paexception.engelsburg.api.endpoint.dto.request.CreateTaskRequestDTO;
import io.github.paexception.engelsburg.api.endpoint.dto.request.GetTasksRequestDTO;
import io.github.paexception.engelsburg.api.endpoint.dto.request.MarkTaskAsDoneRequestDTO;
import io.github.paexception.engelsburg.api.endpoint.dto.request.UpdateTaskRequestDTO;
import io.github.paexception.engelsburg.api.endpoint.dto.response.GetTasksResponseDTO;
import io.github.paexception.engelsburg.api.spring.paging.AbstractPageable;
import io.github.paexception.engelsburg.api.spring.paging.Paging;
import io.github.paexception.engelsburg.api.util.Error;
import io.github.paexception.engelsburg.api.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static io.github.paexception.engelsburg.api.util.Constants.Task.NAME_KEY;

/**
 * Controller for tasks.
 */
@Component
public class TaskController extends AbstractPageable implements UserDataHandler {

	@Autowired
	private TaskRepository taskRepository;

	public TaskController() {
		super(1, 50);
	}

	/**
	 * Create a new task.
	 *
	 * @param dto with task information
	 * @param jwt with userId
	 * @return created task
	 */
	public Result<TaskDTO> createTask(CreateTaskRequestDTO dto, DecodedJWT jwt) {
		return Result.of(this.taskRepository.save(
				new TaskModel(
						-1,
						UUID.fromString(jwt.getSubject()),
						dto.getTitle(),
						dto.getCreated() < 0 ? System.currentTimeMillis() : dto.getCreated(),
						dto.getDue(),
						dto.getSubject(),
						dto.getContent(),
						false
				)
		).toResponseDTO());
	}

	/**
	 * Update a specific task (taskId needed).
	 *
	 * @param dto with task information
	 * @param jwt with userId
	 * @return updated task
	 */
	public Result<TaskDTO> updateTask(UpdateTaskRequestDTO dto, DecodedJWT jwt) {
		UUID userId = UUID.fromString(jwt.getSubject());
		if (dto.getTaskId() < 0) return Result.of(Error.INVALID_PARAM, NAME_KEY);
		Optional<TaskModel> optionalTask = this.taskRepository.findById(dto.getTaskId());
		if (optionalTask.isEmpty()) return Result.of(Error.NOT_FOUND, NAME_KEY);
		if (!optionalTask.get().getUserId().equals(userId)) Result.of(Error.FORBIDDEN, NAME_KEY);

		TaskModel task = optionalTask.get();
		if (dto.getTitle() != null && !dto.getTitle().isBlank()) task.setTitle(dto.getTitle());
		if (dto.getDue() >= 0) task.setDue(dto.getDue());
		if (dto.getSubject() != null && !dto.getSubject().isBlank()) task.setSubject(dto.getSubject());
		if (dto.getContent() != null && !dto.getContent().isBlank()) task.setContent(dto.getContent());

		return Result.of(this.taskRepository.save(task).toResponseDTO());
	}

	/**
	 * Get all tasks.
	 * <p>
	 * Possible params are date after and only done tasks.
	 * </p>
	 *
	 * @param dto    for params
	 * @param jwt    with userId
	 * @param paging paging options
	 * @return list of taskDTOs
	 */
	@Transactional
	public Result<GetTasksResponseDTO> getTasks(GetTasksRequestDTO dto, DecodedJWT jwt, Paging paging) {
		UUID userId = UUID.fromString(jwt.getSubject());
		Stream<TaskModel> taskStream;
		if (dto.isOnlyUndone()) {
			if (dto.getDate() < 0) {
				taskStream = this.taskRepository.findAllByUserIdAndCreatedBeforeAndDoneOrderByCreatedDesc(userId, System.currentTimeMillis(), false, this.toPage(paging));
			} else {
				taskStream = this.taskRepository.findAllByUserIdAndCreatedAfterAndDoneOrderByCreatedAsc(userId, dto.getDate(), false, this.toPage(paging));
			}
		} else {
			if (dto.getDate() < 0) {
				taskStream = this.taskRepository.findAllByUserIdAndCreatedBeforeOrderByCreatedDesc(userId, System.currentTimeMillis(), this.toPage(paging));
			} else {
				taskStream = this.taskRepository.findAllByUserIdAndCreatedAfterOrderByCreatedAsc(userId, dto.getDate(), this.toPage(paging));
			}
		}

		List<TaskDTO> dtos = taskStream.map(TaskModel::toResponseDTO).collect(Collectors.toList());
		if (dtos.isEmpty()) return Result.of(Error.NOT_FOUND, NAME_KEY);
		else return Result.of(new GetTasksResponseDTO(dtos));
	}

	/**
	 * Mark a task as done or undone.
	 *
	 * @param dto with taskId and value to be set for done (default = true)
	 * @param jwt with userId
	 * @return empty result
	 */
	public Result<?> markAsDone(MarkTaskAsDoneRequestDTO dto, DecodedJWT jwt) {
		UUID userId = UUID.fromString(jwt.getSubject());
		Optional<TaskModel> optionalTask = this.taskRepository.findById(dto.getTaskId());
		if (optionalTask.isEmpty()) return Result.of(Error.NOT_FOUND, NAME_KEY);
		if (!optionalTask.get().getUserId().equals(userId)) Result.of(Error.FORBIDDEN, NAME_KEY);

		this.taskRepository.save(optionalTask.get().markAsDone(dto.isDone()));

		return Result.empty();
	}

	/**
	 * Delete a task.
	 *
	 * @param taskId of task to delete
	 * @param jwt    with userId
	 * @return empty result
	 */
	@Transactional
	public Result<?> deleteTask(int taskId, DecodedJWT jwt) {
		UUID userId = UUID.fromString(jwt.getSubject());
		Optional<TaskModel> optionalTask = this.taskRepository.findById(taskId);
		if (optionalTask.isEmpty()) return Result.of(Error.NOT_FOUND, NAME_KEY);

		TaskModel task = optionalTask.get();
		if (!task.getUserId().equals(userId)) return Result.of(Error.FORBIDDEN, NAME_KEY);
		else {
			this.taskRepository.delete(task);
			return Result.empty();
		}
	}


	@Override
	public void deleteUserData(UUID userId) {
		this.taskRepository.deleteAllByUserId(userId);
	}

	@Override
	public Object[] getUserData(UUID userId) {
		return this.mapData(this.taskRepository.findAllByUserId(userId));
	}

}
