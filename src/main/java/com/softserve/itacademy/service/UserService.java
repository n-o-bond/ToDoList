package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.UserRequestDto;
import com.softserve.itacademy.dto.UserResponseDto;
import com.softserve.itacademy.model.User;

import java.util.List;

public interface UserService {
    boolean saveUser(UserRequestDto userRequestDto);
    User findByLogin(String login);
    UserResponseDto findByLoginAndPassword(UserRequestDto userRequestDto);
    String getExpirationLocalDate();

    List<RoleResponse> getAllRoles;
    User readById(long id);
    User update(User user);
    void delete(long id);
    List<User> getAll();

}
