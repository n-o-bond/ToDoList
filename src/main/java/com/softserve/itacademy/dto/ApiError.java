package com.softserve.itacademy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private HttpStatus httpStatus;
    private LocalDateTime localDateTime;
    private String message;
}