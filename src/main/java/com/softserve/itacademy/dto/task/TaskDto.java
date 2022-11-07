package com.softserve.itacademy.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotBlank(message = "The 'name' cannot be empty")
    private String name;

    @NotNull
    private String priority;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long todoId;

    @NotNull
    private String state;
}
