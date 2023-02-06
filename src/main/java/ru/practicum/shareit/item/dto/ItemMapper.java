package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoForBooking toItemDtoForBooking(Item item, List<BookingSmallDto> bookings) {
        ItemDtoForBooking itemDtoForBooking = new ItemDtoForBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
        if ((bookings != null) && (bookings.size() >= 2)) {
            List<BookingSmallDto> bookingsOneItem = bookings.stream()
                    .filter(bookingSmallDto -> Objects.equals(bookingSmallDto.getItemId(), item.getId()))
                    .collect(Collectors.toList());

            if ((bookingsOneItem != null) && (bookingsOneItem.size() >= 2)) {
                itemDtoForBooking.setLastBooking(bookings.get(0));
                itemDtoForBooking.setNextBooking(bookings.get(1));
            }
        }
        return itemDtoForBooking;
    }

    public static Item dtoToItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                //изменить после реализации функционала request
                null
        );
    }
}

