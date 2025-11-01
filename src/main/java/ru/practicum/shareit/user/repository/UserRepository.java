package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    Optional<User> get(Long userId);

    void delete(Long getId);

    boolean existsById(Long getId);

    boolean existsByEmail(String email);
}
