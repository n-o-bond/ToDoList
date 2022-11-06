package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
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
import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping
    public ResponseEntity<TaskDto> create(@PathVariable("todo_id") long todoId, @PathVariable("user_id") long userId,
                                 @Validated @RequestBody TaskDto taskDto) {

        Task task = TaskTransformer.convertToEntity(taskDto, todoService.readById(todoId), stateService.readById(taskDto.getStateId()));
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

    @PutMapping
    public ResponseEntity<TaskDto> update(@PathVariable("todo_id") long todoId, @PathVariable("user_id") long userId,
            @Validated @RequestBody TaskDto taskDto)  {
        Task task = taskService.readById(taskDto.getId());
        taskService.update(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .buildAndExpand(task.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(TaskTransformer.convertToDto(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("todo_id") long todoId, @PathVariable("user_id") long userId,
                                    @PathVariable long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
