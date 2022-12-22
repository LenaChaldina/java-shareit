package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    //Добавление новой вещи
    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.findUserById(userId);
        return itemService.addNewItem(userDto, itemDto);
    }

    //Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto putItem(@PathVariable("itemId") Long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.putItem(itemId, itemDto, userId);
    }

    //Просмотр информации о конкретной вещи по её идентификатору.
    //Эндпойнт GET /items/{itemId}. Информацию о вещи может просмотреть любой пользователь.
    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
    //Эндпойнт GET /items.
    @GetMapping
    public List<Item> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUser(userId);
    }

    //Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст,
    //и система ищет вещи, содержащие этот текст в названии или описании.
    // Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    // Проверьте, что поиск возвращает только доступные для аренды вещи.
    @GetMapping("/search")
    public List<Item> getAvailableItems(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemService.getAvailableItems(userId, text);
    }
}
