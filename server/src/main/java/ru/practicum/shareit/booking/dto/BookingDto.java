package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    @Transient
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    UserDto booker;
    ItemDto item;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, Status status, UserDto booker, ItemDto item) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }

    public BookingDto(LocalDateTime start, LocalDateTime end, Status status, UserDto booker, ItemDto item) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.booker = booker;
        this.item = item;
    }
}
