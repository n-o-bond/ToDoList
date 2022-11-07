package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.RoleResponse;
import com.softserve.itacademy.dto.user.UserDto;
import com.softserve.itacademy.model.User;

import java.util.List;

public interface UserService {
     User saveUser(User user);

    User findByLogin(String login);

    UserDto findByLoginAndPassword(UserDto userDto);

    String getExpirationLocalDate();

    List<RoleResponse> getAllRoles();

    User readById(long id);

    User update(User user);

    void delete(long id);

    List<User> getAll();

}
