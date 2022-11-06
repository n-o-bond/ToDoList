package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Controller
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ApiError> handler(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(new ApiError(LocalDateTime.now(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
