package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepositoryInMemory;

    @Override
    public ItemDto addNewItem(UserDto userDto, ItemDto itemDto) {
        Item item = ItemMapper.dtoToItem(itemDto);
        User user = UserMapper.dtoToUser(userDto);
        return ItemMapper.toItemDto(itemRepositoryInMemory.addNewItem(user, item));
    }

    @Override
    public ItemDto putItem(Integer itemId, ItemDto itemDto, Integer userId) {
        Item item = ItemMapper.dtoToItem(itemDto);
        return ItemMapper.toItemDto(itemRepositoryInMemory.putItem(itemId, item, userId));
    }

    @Override
    public Item getItemById(Integer itemId, Integer userId) {
        return itemRepositoryInMemory.getItemById(itemId, userId);
    }

    @Override
    public List<Item> getItemsByUser(Integer userId) {
        return itemRepositoryInMemory.getItemsByUser(userId);
    }

    @Override
    public List<Item> getAvailableItems(Integer userId, String text) {
        return itemRepositoryInMemory.getAvailableItems(userId, text);
    }
}
