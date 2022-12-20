package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    Item addNewItem(User user, Item item);

    Item putItem(Integer itemId, Item item, Integer userId);

    Item getItemById(Integer itemId, Integer userId);

    List<Item> getItemsByUser(Integer userId);

    List<Item> getAvailableItems(Integer userId, String text);
}
