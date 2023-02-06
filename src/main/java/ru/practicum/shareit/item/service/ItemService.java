package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(UserDto userDto, ItemDto itemDto);

    ItemDto putItem(Long itemId, ItemDto itemDto, Long userId);

    ItemDtoForBooking getItemById(Long itemId, User user, List<BookingSmallDto> bookingSmallDto);

    //List<ItemDto> getItemsByUser(UserDto userDto);
    List<ItemDtoForBooking> getItemsByUser(UserDto userDto, List<BookingSmallDto> bookings);
    List<ItemDto> search(Long userId, String text);

    void checkItemsAvailability(Long id);
    Item getItemByOwner(Long itemId);
}
