package com.softserve.itacademy.dto.user;

import com.softserve.itacademy.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer {

    public static UserDto convertToDto(User user) {
        if (user == null)
            return null;

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setLogin(user.getLogin());
        userDto.setRole(user.getRole().getName());

        return userDto;
    }

    public static User convertToEntity(UserDto userDto) {
        if (userDto == null)
            return null;

        User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());

        return user;
    }
}