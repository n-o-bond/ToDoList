package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.task.TaskDto;
import com.softserve.itacademy.dto.task.TaskTransformer;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// /api/todos
// /api/tasks/5 - get - read
// /api/tasks/5 - put - update
// /api/tasks/5{todoId} - post - create
// /api/tasks/5 - delete - delete

@RestController
@RequestMapping("/api/users/{user_id}/todos/{todo_id}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;
    public TaskController(TaskService taskService, ToDoService todoService, StateService stateService) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.stateService = stateService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public List<TaskDto> getAll(@PathVariable("todo_id") long todoId) {
        List<TaskDto> taskDtos = new ArrayList<>();

        for (Task task : todoService.readById(todoId).getTasks())
            taskDtos.add(TaskTransformer.convertToDto(task));

        return taskDtos;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<TaskDto> read(@PathVariable("todo_id") long todoId, @PathVariable long id) {
        return ResponseEntity.ok(TaskTransformer.convertToDto(taskService.readById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<TaskDto> create(@PathVariable("todo_id") long todoId, @PathVariable("user_id") long userId,
                                          @Validated @RequestBody TaskDto taskDto) {

        Task task = TaskTransformer.convertToEntity(taskDto, todoService.readById(todoId), stateService.getByName(taskDto.getState()));
        task = taskService.create(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(TaskTransformer.convertToDto(task));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<TaskDto> update(@PathVariable long id, @PathVariable("todo_id") long todoId, @Validated @RequestBody TaskDto taskDto) {
        Task task = taskService.readById(id);

        task.setName(taskDto.getName());
        task.setState(stateService.getByName(taskDto.getState()));
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        taskService.update(task);

        return ResponseEntity.ok(TaskTransformer.convertToDto(task));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public void delete(@PathVariable("todo_id") long todoId, @PathVariable long id) {
        taskService.delete(id);
    }
}
