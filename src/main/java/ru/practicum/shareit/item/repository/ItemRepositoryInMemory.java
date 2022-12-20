package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemRepositoryInMemory implements ItemRepository {
    private Map<Integer, Item> items = new HashMap<>();
    private int id = 1;

    @Override
    public Item addNewItem(User user, Item item) {
        item.setId(id++);
        item.setOwner(user);
        items.put(item.getId(), item);
        log.info("Вещь с ID:" + item.getId() + " добавлена пользователем с id:" + user.getId());
        return item;
    }

    //Редактирование вещи.
    // Изменить можно название, описание и статус доступа к аренде.
    // Редактировать вещь может только её владелец.
    @Override
    public Item putItem(Integer itemId, Item item, Integer userId) {
        checkItemId(itemId);
        checkUserId(itemId, userId);
        if (item.getName() != null) {
            items.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(itemId).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.get(itemId).setAvailable(item.getAvailable());
        }
        log.info("Вещь с ID:" + itemId + " обновлена пользователем с id:" + userId);
        return items.get(itemId);
    }

    //Просмотр информации о конкретной вещи по её идентификатору.
    //Информацию о вещи может просмотреть любой пользователь.
    @Override
    public Item getItemById(Integer itemId, Integer userId) {
        checkItemId(itemId);
        log.info("Вещь с ID:" + itemId + " успешно найдена");
        return items.get(itemId);
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
    @Override
    public List<Item> getItemsByUser(Integer userId) {
        List<Item> itemsForOwner = new ArrayList<>();
        itemsForOwner = items.values().stream().filter(item -> item.getOwner().getId().equals(userId)).collect(Collectors.toList());
        log.info("Найдены все вещи пользователя с id:" + userId);
        return itemsForOwner;
    }

    //Поиск вещи потенциальным арендатором.
    //Пользователь передаёт в строке запроса текст, и система ищет вещи,
    //содержащие этот текст в названии или описании.
    //Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    //Проверьте, что поиск возвращает только доступные для аренды вещи.
    @Override
    public List<Item> getAvailableItems(Integer userId, String text) {
        String textToLower = text.toLowerCase();
        List<Item> itemsForUser = new ArrayList<>();
        if (!text.isBlank()) {
            itemsForUser = items.values()
                    .stream()
                    .filter(item -> item.getAvailable() == true)
                    .filter(item -> item.getName().toLowerCase().contains(textToLower)
                            || item.getDescription().toLowerCase().contains(textToLower))
                    .collect(Collectors.toList());
        }
        return itemsForUser;
    }

    private Boolean checkItemId(Integer itemId) {
        if (items.containsKey(itemId)) {
            return true;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Вещи c id:" + itemId + " нет в списке");
        }
    }

    private Boolean checkUserId(Integer itemId, Integer userId) {
        if (items.get(itemId).getOwner().getId().equals(userId)) {
            return true;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Редактировать вещь может только её владелец");
        }
    }
}
