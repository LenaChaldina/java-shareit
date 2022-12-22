package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(UserDto userDto, ItemDto itemDto);

    ItemDto putItem(Long itemId, ItemDto itemDto, Long userId);

    Item getItemById(Long itemId, Long userId);

    List<Item> getItemsByUser(Long userId);

    List<Item> getAvailableItems(Long userId, String text);
}
