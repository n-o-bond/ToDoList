package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.OperationResponse;
import com.softserve.itacademy.dto.UserRequestDto;
import com.softserve.itacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RegistrationController {
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/index"})
    public OperationResponse signUp() {
        return new OperationResponse(String.valueOf(true));
    }

    @PostMapping("/signup")
    public OperationResponse signUp(
            @RequestParam(value = "login", required = true) String login,
            @RequestParam(value = "password", required = true) String password) {
        log.info("**/signup userLogin = " + login);
        UserRequestDto userRequestDto = new UserRequestDto(login, password);
        return new OperationResponse(String.valueOf(userService.saveUser(userRequestDto)));
    }
}
