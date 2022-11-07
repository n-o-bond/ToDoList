package com.softserve.itacademy.dto.toDo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotBlank
    String title;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long ownerId;
}
