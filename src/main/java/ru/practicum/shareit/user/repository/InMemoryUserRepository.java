package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private Long currentId = 1L;
    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(currentId);
        users.put(currentId, user);
        currentId++;
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return !users.values().stream().filter(it -> it.getEmail().equals(email)).toList().isEmpty();
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }
}
