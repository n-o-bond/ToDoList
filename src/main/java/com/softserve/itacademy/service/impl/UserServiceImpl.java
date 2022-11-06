package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.config.CustomUserDetails;
import com.softserve.itacademy.dto.RoleResponse;
import com.softserve.itacademy.dto.UserRequestDto;
import com.softserve.itacademy.dto.UserResponseDto;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.RoleData;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        initData();
    }

    private void initData() {
        if ((roleRepository.count() == 0) && (userRepository.count() == 0)) {
            roleRepository.save(new Role(1, RoleData.USER.toString()));
            Role adminRole = roleRepository.save(new Role(2, RoleData.ADMIN.toString()));
            User admin = new User(1, "admin", passwordEncoder.encode("admin"), adminRole);
            userRepository.save(admin);
        }
    }

    @Override
    public boolean saveUser(UserRequestDto userRequest) {
        User user = new User();
        user.setRole(roleRepository.findByName(RoleData.USER.toString()));
        user.setLogin(userRequest.getLogin());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return (userRepository.save(user) != null);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserResponseDto findByLoginAndPassword(UserRequestDto userRequestDto) {
        UserResponseDto result = null;
        User user = userRepository.findByLogin(userRequestDto.getLogin());
        if ((user != null)
                && (passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword()))) {
            result = new UserResponseDto();
            result.setLogin(userRequestDto.getLogin());
            result.setRoleName(user.getRole().getName());
        }
        return result;
    }

    @Override
    public String getExpirationLocalDate() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime localDate = customUserDetails.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' hh:mm");
        return localDate.format(formatter);
    }

    public List<RoleResponse> getAllRoles() {
        List<RoleResponse> result = new ArrayList<>();
        for (Role role : roleRepository.findAll()) {
            result.add(new RoleResponse(role.getName()));
        }
        return result;
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User update(User role) {
        if (role != null) {
            readById(role.getId());
            return userRepository.save(role);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        userRepository.delete(readById(id));
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }
}
