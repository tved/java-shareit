package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository storage;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        if (storage.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email is already in use");
        }
        User newUser = UserMapper.toUser(request);
        newUser = storage.create(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User existing = storage.get(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        String newEmail = request.getEmail();
        if (newEmail != null && !newEmail.equals(existing.getEmail())) {
            if (storage.existsByEmail(newEmail)) {
                throw new ConflictException("Email is already in use");
            }
        }
        User updated = UserMapper.updateUser(existing, request);
        storage.update(updated);
        return UserMapper.toUserDto(updated);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = storage.get(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!existsById(id)) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        storage.delete(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.existsById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return storage.getAll().stream().map(UserMapper::toUserDto).toList();
    }
}
