package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    final Integer id;
    //текст запроса, содержащий описание требуемой вещи
    final String description;
    //пользователь, создавший запрос
    final User requester;
    //владелец вещи
    final LocalDateTime created;
}
