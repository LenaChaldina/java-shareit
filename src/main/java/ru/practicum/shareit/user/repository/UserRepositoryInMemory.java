package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserRepositoryInMemory implements UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User addUser(User user) {
        if (!checkEmailDuplicate(user)) {
            throw new RequestError(HttpStatus.CONFLICT, "Юзер с такой почтой уже создан");
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с id:" + user.getId());
        return user;
    }

    @Override
    public User putUser(User user, Long userId) {
        //должна быть возможность земенить не только всего юзера но и почту либо имя у существующего
        if (users.containsKey(userId)) {
            if (checkEmailDuplicate(user)) {
                if (user.getName() == null) {
                    user.setName(users.get(userId).getName());
                    user.setId(userId);
                }
                if (user.getEmail() == null) {
                    user.setEmail(users.get(userId).getEmail());
                    user.setId(userId);
                }
                user.setId(userId);
                users.put(user.getId(), user);
                log.info("Обновлен пользователь с id:" + userId);
                return user;
            } else {
                throw new RequestError(HttpStatus.CONFLICT, "Юзер с такой почтой уже создан");
            }
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Taкого юзера нет в списке");
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> usersForGet = new ArrayList<>();
        usersForGet.addAll(users.values());
        return usersForGet;
    }

    @Override
    public User findUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Taкого юзера нет в списке");
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            log.info("Удален пользователь с id:" + id);
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Taкого юзера нет в списке");
        }
    }

    private boolean checkEmailDuplicate(User user) {
        List<User> usersDuplicate = new ArrayList<>();
        usersDuplicate.addAll(users.values().stream().filter(user1 -> user1.getEmail().equals(user.getEmail())).collect(Collectors.toList()));
        if (usersDuplicate.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
