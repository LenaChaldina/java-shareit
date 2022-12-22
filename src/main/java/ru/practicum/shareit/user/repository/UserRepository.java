package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    User putUser(User user, Long userId);

    List<User> getUsers();

    User findUserById(Long id);

    void deleteUser(Long id);
}
