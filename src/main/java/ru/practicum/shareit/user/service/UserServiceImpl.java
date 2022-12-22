package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepositoryInMemory;

    @Autowired
    public UserServiceImpl(UserRepository userRepositoryInMemory) {
        this.userRepositoryInMemory = userRepositoryInMemory;
    }

    @Override
    public UserDto addUser(User user) {
        return UserMapper.toUserDto(userRepositoryInMemory.addUser(user));
    }

    @Override
    public UserDto putUser(User user, Long userId) {
        return UserMapper.toUserDto(userRepositoryInMemory.putUser(user, userId));
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepositoryInMemory.getUsers();
        return users.stream().map(user -> UserMapper.toUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        return UserMapper.toUserDto(userRepositoryInMemory.findUserById(id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepositoryInMemory.deleteUser(id);
    }
}
