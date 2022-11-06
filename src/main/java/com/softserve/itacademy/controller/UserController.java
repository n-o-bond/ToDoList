package com.softserve.itacademy.controller;


import com.softserve.itacademy.config.JwtProvider;
import com.softserve.itacademy.dto.OperationResponse;
import com.softserve.itacademy.dto.RoleResponse;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/user/date")
    public OperationResponse expirationDate() {
        log.info("**/user/date");
        return new OperationResponse("Expiration date is " + userService.getExpirationLocalDate());
    }

    @GetMapping("/admin/roles")
    public List<RoleResponse> listRoles() {
        log.info("**/admin/roles");
        return userService.getAllRoles();
    }

    @GetMapping("/user/{id}/read")
    public User read(@PathVariable long id) throws EntityNotFoundException {
        log.info("**/user/{id}/read where id = " + id);
        return userService.readById(id);
    }


    @GetMapping("/user/{id}/delete")
    public void delete(@PathVariable("id") long id) throws EntityNotFoundException {
        log.info("**/user/{id}/delete where id = " + id);
        userService.delete(id);
    }

    @GetMapping("/admin/all")
    public List<User> getAll() {
        log.info("**/admin/all");
        return userService.getAll();
    }

    @PutMapping("/user/{id}/update")
    public User update(@PathVariable("id") long id, @RequestBody User user) {
        log.info("**/user/{id}/update where id = " + id);
        return userService.update(user);
    }
}
