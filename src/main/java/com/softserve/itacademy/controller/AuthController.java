package com.softserve.itacademy.controller;

import com.softserve.itacademy.config.JwtProvider;
import com.softserve.itacademy.dto.OperationResponse;
import com.softserve.itacademy.dto.auth.AuthRequestDto;
import com.softserve.itacademy.dto.auth.AuthResponseDto;
import com.softserve.itacademy.dto.user.UserDto;
import com.softserve.itacademy.dto.user.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;


    @PostMapping(value={"/", "/signin"})
    public AuthResponseDto signIn(@RequestBody @Valid AuthRequestDto authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                authRequest.getLogin(),
                authRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return new AuthResponseDto(jwtProvider.generateToken(authentication.getPrincipal().toString()));
    }

    @PostMapping("/signup")
    public OperationResponse signUp(@RequestBody UserDto userDto) {
        User user = userService.saveUser(UserTransformer.convertToEntity(userDto));
        if(user!=null)
            return new OperationResponse(String.valueOf(true));
        else  return new OperationResponse(String.valueOf(false));
    }
}
