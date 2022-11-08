package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.user.UserDto;
import com.softserve.itacademy.dto.user.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping({"/", "home"})
    public List<UserDto> getAll() {
        List<UserDto> users= new ArrayList<>();
        for(User user:userService.getAll())
            users.add(UserTransformer.convertToDto(user));
        return users;
    }
}
