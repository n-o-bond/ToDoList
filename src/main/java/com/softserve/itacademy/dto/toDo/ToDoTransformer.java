package com.softserve.itacademy.dto.toDo;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;

public class ToDoTransformer {
    public static ToDoDto convertToDto(ToDo toDo)
    {
        ToDoDto toDoDto = new ToDoDto();
        toDoDto.setId(toDo.getId());
        toDoDto.setTitle(toDo.getTitle());
        toDoDto.setCreatedAt(toDo.getCreatedAt());
        toDoDto.setOwnerId(toDo.getOwner().getId());

        return toDoDto;
    }

    public static ToDo convertToEntity(ToDoDto toDoDto, User owner){
        ToDo toDo = new ToDo();
        toDo.setTitle(toDoDto.getTitle());
        toDo.setOwner(owner);
        toDo.setCreatedAt(toDoDto.getCreatedAt());
        return toDo;
    }
}

