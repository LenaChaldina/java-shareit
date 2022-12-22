package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    Item addNewItem(User user, Item item);

    Item putItem(Long itemId, Item item, Long userId);

    Item getItemById(Long itemId, Long userId);

    List<Item> getItemsByUser(Long userId);

    List<Item> getAvailableItems(Long userId, String text);
}
