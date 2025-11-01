package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static User toUser(CreateUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public static User updateUser(User existing, UpdateUserRequest request) {
        return existing.toBuilder()
                .email(request.getEmail() != null ? request.getEmail() : existing.getEmail())
                .name(request.getName() != null ? request.getName() : existing.getName())
                .build();
    }

}
