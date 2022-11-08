package com.softserve.itacademy.controller;


import com.softserve.itacademy.dto.toDo.ToDoDto;
import com.softserve.itacademy.dto.toDo.ToDoTransformer;
import com.softserve.itacademy.dto.user.UserTransformer;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users/{owner_id}/todos")
public class ToDoController {

    @Autowired
    private final ToDoService todoService;
    @Autowired
    private final UserService userService;

    public ToDoController(ToDoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or #ownerId == principal")
    public List<ToDoDto> getAll(@PathVariable("owner_id") long ownerId) {
        log.info("**/all/users/{user_id} + where user_id = " + ownerId);
        List<ToDoDto> toDoDtos = new ArrayList<>();

        for (ToDo toDo : todoService.getByUserId(ownerId)) {
            toDoDtos.add(ToDoTransformer.convertToDto(toDo));
        }

        return toDoDtos;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#id) >= 1")
    public ResponseEntity<ToDoDto> read(@PathVariable long id) {
        log.info("**/user/{id}/read where id = " + id);
        return ResponseEntity.ok(ToDoTransformer.convertToDto(todoService.readById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#id) == 1")
    public ResponseEntity<ToDoDto> update(@PathVariable long id, @PathVariable("owner_id") long userId,
                                                  @RequestBody ToDoDto toDoDto) {
        log.info("**/{todo_id}/update/users/{owner_id} where todo_id = " + id);

        ToDo toDo = todoService.readById(id);
        toDo.setTitle(toDoDto.getTitle());
        todoService.update(toDo);

        return ResponseEntity.ok(ToDoTransformer.convertToDto(toDo));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or #ownerId == principal")
    public ResponseEntity<ToDoDto> create(@PathVariable("owner_id") long ownerId,
                                          @Validated @RequestBody ToDoDto toDoDto) {
        log.info("**/create/users/{owner_id} where owner_id = " + ownerId);
        toDoDto.setCreatedAt(LocalDateTime.now());
        ToDo toDo = ToDoTransformer.convertToEntity(toDoDto, userService.readById(ownerId));
        toDo = todoService.create(toDo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(toDo.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ToDoTransformer.convertToDto(toDo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or  @toDoController.isOwnerOrCollaborator(#id) == 1")
    public void delete(@PathVariable long id, @PathVariable("owner_id") long ownerId) {
        log.info("**/{todo_id}/delete/users/{owner_id} where todo_id = " + id);
        todoService.delete(id);
    }

    @GetMapping("/{todo_id}/collaborators")
    @PreAuthorize("hasAuthority('ADMIN') or @toDoController.isOwnerOrCollaborator(#todoId) >= 1")
    public ResponseEntity<?> getAllCollaborators(@PathVariable("todo_id") long todoId,
                                                 @PathVariable("owner_id") long ownerId) {
        ToDo todo = todoService.readById(todoId);
        return ResponseEntity.ok(todo.getCollaborators()
                .stream()
                .map(UserTransformer::convertToDto)
                .collect(Collectors.toList()));

    }

    @PostMapping("/{todo_id}/collaborators/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or  @toDoController.isOwnerOrCollaborator(#toDoId) == 1")
    public ResponseEntity<?> addCollaborator(@PathVariable("owner_id") long ownerId,
                                             @PathVariable("todo_id") long toDoId,
                                             @PathVariable long id) {
        log.info("**/{id}/collaborators");
        ToDo todo = todoService.readById(toDoId);
        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(id));
        todo.setCollaborators(collaborators);
        todoService.update(todo);

        return ResponseEntity.ok(collaborators.stream()
                        .map(UserTransformer::convertToDto)
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("/{todo_id}/collaborators/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or  @toDoController.isOwnerOrCollaborator(#toDoId) == 1")
    public ResponseEntity<?> removeCollaborator(@PathVariable("todo_id") long toDoId,
                                                @PathVariable("owner_id") long ownerId,
                                                @PathVariable long id) {
        log.info("**/{id}/remove");
        ToDo todo = todoService.readById(toDoId);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(id));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
        return ResponseEntity.ok(collaborators.stream()
                        .map(UserTransformer::convertToDto)
                        .collect(Collectors.toList()));

    }


    public int isOwnerOrCollaborator(long todoId){

        long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ToDo toDo = todoService.readById(todoId);
        if(toDo.getOwner().getId() == currentUserId) {
            return 1;
        }
        for(User user: toDo.getCollaborators()){
            if(user.getId() == currentUserId){
                return 2;
            }
        }

        return 0;
    }

}