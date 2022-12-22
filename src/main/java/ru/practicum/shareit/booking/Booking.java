package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    Long id;
    //дата и время начала бронирования;
    final LocalDateTime start;
    //дата и время конца бронирования
    final LocalDateTime end;
    //вещь, которую пользователь бронирует
    final Item item;
    //пользователь, который осуществляет бронирование
    final User booker;
    //статус бронирования
    Status status;
}
