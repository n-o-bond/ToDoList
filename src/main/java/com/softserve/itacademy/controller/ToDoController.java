package com.softserve.itacademy.controller;


import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users/{user-id}/todos")
public class ToDoController {

    @Autowired
    private final ToDoService todoService;
    @Autowired
    private final UserService userService;

    public ToDoController(ToDoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }


    @PostMapping("/create/users/{owner_id}")
    public ToDo create(@PathVariable("owner_id") long ownerId, @Validated @ModelAttribute("todo") ToDo todo) {
        log.info("**/create/users/{owner_id} where owner_id = " + ownerId);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setOwner(userService.readById(ownerId));
        return todoService.create(todo);
    }

    @GetMapping("/{id}/tasks")
    public ToDo read(@PathVariable long id, Model model) {
        log.info("**/{id}/tasks where id = " + id);
        return todoService.readById(id);
    }

    @PutMapping("/{todo_id}/update/users/{owner_id}")
    public ToDo update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId,
                       @Validated @ModelAttribute("todo") ToDo todo, BindingResult result) {
        log.info("**/{todo_id}/update/users/{owner_id} where todo_id = " + todoId);
        return todoService.update(todo);
    }

    @DeleteMapping("/{todo_id}/delete/users/{owner_id}")
    public void delete(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId) {
        log.info("**/{todo_id}/delete/users/{owner_id} where todo_id = " + todoId);
        todoService.delete(todoId);
    }

    @GetMapping("/all/users/{user_id}")
    public List<ToDo> getAll(@PathVariable("user_id") long userId, Model model) {
        log.info("**/all/users/{user_id} + where user_id = " + userId);
        return todoService.getByUserId(userId);
    }

    @PostMapping("/{id}/collaborators")
    public ToDo addCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        log.info("**/{id}/collaborators");
        ToDo todo = todoService.readById(id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.add(userService.readById(userId));
        todo.setCollaborators(collaborators);
        return todoService.update(todo);
    }

    @DeleteMapping("/{id}/remove")
    public void removeCollaborator(@PathVariable long id, @RequestParam("user_id") long userId) {
        log.info("**/{id}/remove");
        ToDo todo = todoService.readById(id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(userId));
        todo.setCollaborators(collaborators);
        todoService.update(todo);
    }
}