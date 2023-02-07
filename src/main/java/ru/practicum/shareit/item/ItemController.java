package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    private final BookingService bookingService;

    //Добавление новой вещи
    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.findUserById(userId);
        return itemService.addNewItem(userDto, itemDto);
    }

    //Комментарий можно добавить по эндпоинту POST /items/{itemId}/comment
    //Не забудьте добавить проверку, что пользователь, который пишет комментарий, действительно брал вещь в аренду.
    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable Long itemId, @RequestBody Comment comment) {
        return itemService.createComment(userId, itemId, comment.getText());
    }

    //Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto putItem(@PathVariable("itemId") Long itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.putItem(itemId, itemDto, userId);
    }

    //Просмотр информации о конкретной вещи по её идентификатору.
    //Эндпойнт GET /items/{itemId}. Информацию о вещи может просмотреть любой пользователь.
    //нужно, чтобы владелец видел даты последнего и ближайшего следующего бронирования для каждой вещи,
    //когда просматривает список (GET /items).
    //Отзывы можно будет увидеть по двум эндпоинтам — по GET /items/{itemId} для одной конкретной вещи
    //и по GET /items для всех вещей данного пользователя.
    @GetMapping("/{itemId}")
    public ItemDtoForBooking getItemById(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        UserDto userDto = userService.findUserById(userId);
        User user = UserMapper.dtoToUser(userDto);
        List<BookingSmallDto> bookingsSmallDto = null;
        List<CommentResponseDto> commentsResponseDto;
        commentsResponseDto = itemService.getCommentList(itemId);
        if (Objects.equals(itemService.getItemByOwner(itemId).getOwner().getId(), (userId))) {
            bookingsSmallDto = bookingService.getBookingsByItem(itemId);
        }
        return itemService.getItemById(itemId, user, bookingsSmallDto, commentsResponseDto);
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
    //Эндпойнт GET /items.
    @GetMapping
    public List<ItemDtoForBooking> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        UserDto userDto = userService.findUserById(userId);
        List<BookingSmallDto> bookings = bookingService.getBookingsByOwner(userDto);
        List<CommentResponseDto> commentsResponseDto = null;
        return itemService.getItemsByUser(userDto, bookings, null);
    }

    //Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст,
    //и система ищет вещи, содержащие этот текст в названии или описании.
    // Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    // Проверьте, что поиск возвращает только доступные для аренды вещи.
    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemService.search(userId, text);
    }
}
