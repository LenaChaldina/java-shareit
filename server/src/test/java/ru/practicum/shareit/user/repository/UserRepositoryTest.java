package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    Long userIdElena;
    List<Long> ids;
    User userInputElena;

    @BeforeEach
    private void addUser() {

        userInputElena = new User(userIdElena, "Elena", "chald.e@gmail.com");
        userRepository.save(userInputElena);

        List<User> allUsers = userRepository.findAll();
        assertEquals(1, allUsers.size());
        userIdElena = allUsers.get(0).getId();
        ids = List.of(userIdElena);
    }

    @Test
    void findByUserIds() {
        List<User> users = userRepository.findByUserIds(ids);
        assertEquals(1, users.size());
        assertEquals(userIdElena, users.get(0).getId());
        assertEquals("Elena", users.get(0).getName());
        assertEquals("chald.e@gmail.com", users.get(0).getEmail());
    }

    @AfterEach
    private void deleteUsers() {
        userRepository.deleteAll();
    }
}