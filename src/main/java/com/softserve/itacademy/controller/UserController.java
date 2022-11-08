package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.*;
import com.softserve.itacademy.dto.user.UserDto;
import com.softserve.itacademy.dto.user.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/user/date")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OperationResponse expirationDate() {
        log.info("**/user/date");
        return new OperationResponse("Expiration date is " + userService.getExpirationLocalDate());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/roles")
    public List<RoleResponse> listRoles() {
        log.info("**/admin/roles");
        return userService.getAllRoles();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<UserDto> getAll() {
        log.info("**/admin/all");
        List<UserDto> users= new ArrayList<>();
        for(User user:userService.getAll())
            users.add(UserTransformer.convertToDto(user));
        return users;
    }

    @PreAuthorize("hasAuthority('ADMIN') || principal == #id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> read(@PathVariable long id) throws EntityNotFoundException {
        log.info("**/user/{id}/read where id = " + id);
        return ResponseEntity.ok(UserTransformer.convertToDto(userService.readById(id)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        User user = UserTransformer.convertToEntity(userDto);
        user = userService.saveUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(UserTransformer.convertToDto(user));
    }

    @PreAuthorize("hasAuthority('ADMIN') || principal == #id")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        log.info("**/user/{id}/update where id = " + id);
        User user = UserTransformer.convertToEntity(userDto);
        user.setId(id);
        user.setRole(roleService.readById(2));
        userService.update(user);

        return ResponseEntity.ok(UserTransformer.convertToDto(user));
    }

    @PreAuthorize("hasAuthority('ADMIN') || principal == #id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) throws EntityNotFoundException {
        log.info("**/user/{id}/delete where id = " + id);
        userService.delete(id);
    }
}
