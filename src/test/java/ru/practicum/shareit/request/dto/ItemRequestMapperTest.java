package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

class ItemRequestMapperTest {
    User userInput = new User(1L, "Elena", "chaldina.e@gmail.com");
    List<Item> items = List.of();
    List<ItemDtoForRequest> itemDtoForRequests = List.of();
    private ItemRequest itemRequest = new ItemRequest(1L, "desc", userInput, LocalDateTime.now().withNano(0), items);
    private ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "desc", 1L, LocalDateTime.now().withNano(0), itemDtoForRequests);

    @Test
    void toItemRequestDto() {
        ItemRequestMapper.toItemRequestDto(itemRequest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(itemRequestDto.getId(), 1L),
                () -> Assertions.assertEquals(itemRequestDto.getDescription(), "desc"),
                () -> Assertions.assertEquals(itemRequestDto.getRequesterId(), 1L),
                () -> Assertions.assertEquals(itemRequestDto.getCreated(), LocalDateTime.now().withNano(0)),
                () -> Assertions.assertEquals(itemRequestDto.getItems(), itemDtoForRequests)
        );
    }

    @Test
    void toDtoItemRequest() {
        ItemRequestMapper.toDtoItemRequest(itemRequestDto, userInput);
        Assertions.assertAll(
                () -> Assertions.assertEquals(itemRequest.getId(), 1L),
                () -> Assertions.assertEquals(itemRequest.getDescription(), "desc"),
                () -> Assertions.assertEquals(itemRequest.getRequester(), userInput),
                () -> Assertions.assertEquals(itemRequest.getCreated(), LocalDateTime.now().withNano(0)),
                () -> Assertions.assertEquals(itemRequest.getItems(), items)
        );
    }
}