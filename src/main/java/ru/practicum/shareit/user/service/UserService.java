package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserRequest newUser);

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UpdateUserRequest user);

    void deleteUser(Long id);

    boolean existsById(Long id);

    List<UserDto> getUsers();
}
