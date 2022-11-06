package com.softserve.itacademy.controller;

import com.softserve.itacademy.config.JwtProvider;
import com.softserve.itacademy.dto.TokenResponse;
import com.softserve.itacademy.dto.UserRequestDto;
import com.softserve.itacademy.dto.UserResponseDto;
import com.softserve.itacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class LoginController {
    private UserService userService;

    private JwtProvider jwtProvider;

    @Autowired
    public LoginController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public TokenResponse signIn(
            @RequestParam(value = "login", required = true) String login,
            @RequestParam(value = "password", required = true) String password) {
        log.info("**/signin userLogin = " + login);
        UserRequestDto userRequestDto = new UserRequestDto(login, password);
        UserResponseDto userResponseDto = userService.findByLoginAndPassword(userRequestDto);
        return new TokenResponse(jwtProvider.generateToken(userResponseDto.getLogin()));
    }
}
