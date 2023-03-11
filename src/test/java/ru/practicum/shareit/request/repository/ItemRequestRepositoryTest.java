package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRequestRepositoryTest {
    Long userIdElena;
    Long userIdDaria;
    Long requestIdByElena;
    Long requestIdByDaria;
    User userInputElena;
    User userInputDaria;
    ItemRequest itemRequestByElena;
    ItemRequest itemRequestByDaria;
    PageRequest pageRequest;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeAll
    private void addItemRequest() {
        userIdElena = 1L;
        userIdDaria = 2L;
        requestIdByElena = 1L;
        requestIdByDaria = 2L;
        userInputElena = new User(userIdElena, "Elena", "chaldina.e@gmail.com");
        userInputDaria = new User(userIdDaria, "Daria", "vilkova.d@gmail.com");
        itemRequestByElena = new ItemRequest(requestIdByElena, "desc", userInputElena, LocalDateTime.now().withNano(0), null);
        itemRequestByDaria = new ItemRequest(requestIdByDaria, "desc1", userInputDaria, LocalDateTime.now().withNano(0), null);
        pageRequest = PageRequest.of(0, 10);

        userRepository.save(userInputElena);
        userRepository.save(userInputDaria);
        itemRequestRepository.save(itemRequestByElena);
        itemRequestRepository.save(itemRequestByDaria);
    }

    @Test
    void findRequestsWithoutOwner() {
        Page<ItemRequest> actualItemRequest = itemRequestRepository.findRequestsWithoutOwner(userIdElena, pageRequest);
        assertEquals(1, actualItemRequest.getContent().size());
        assertEquals(userIdDaria, actualItemRequest.getContent().get(0).getId());
    }

    @Test
    void findRequestsByUser() {
        List<ItemRequest> actualItemRequest = itemRequestRepository.findRequestsByUser(userIdElena);
        assertEquals(1, actualItemRequest.size());
        assertEquals(1, actualItemRequest.get(0).getId());
        assertEquals("desc", actualItemRequest.get(0).getDescription());
    }

    @AfterAll
    private void deleteItemRequests() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }
}