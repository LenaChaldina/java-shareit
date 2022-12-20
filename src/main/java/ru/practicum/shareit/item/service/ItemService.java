package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(UserDto userDto, ItemDto itemDto);

    ItemDto putItem(Integer itemId, ItemDto itemDto, Integer userId);

    Item getItemById(Integer itemId, Integer userId);

    List<Item> getItemsByUser(Integer userId);

    List<Item> getAvailableItems(Integer userId, String text);
}
