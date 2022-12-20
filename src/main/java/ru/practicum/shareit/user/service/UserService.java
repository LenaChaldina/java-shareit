package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto addUser(User user);

    UserDto putUser(User user, int userId);

    List<UserDto> getUsers();

    UserDto findUserById(int id);

    void deleteUser(int id);
}
