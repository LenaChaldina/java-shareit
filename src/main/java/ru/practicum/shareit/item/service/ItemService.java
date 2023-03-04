package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(UserDto userDto, ItemDto itemDto);

    ItemDto putItem(Long itemId, ItemDto itemDto, Long userId);

    //ItemDtoForBooking getItemById(Long itemId, User user, List<BookingSmallDto> bookingSmallDto);
    ItemDtoForBooking getItemById(Long itemId, User user, List<BookingSmallDto> bookingSmallDto, List<CommentResponseDto> commentsResponseDto);

   // List<ItemDtoForBooking> getItemsByUser(UserDto userDto, List<BookingSmallDto> bookings);
    List<ItemDtoForBooking> getItemsByUser(UserDto userDto, List<BookingSmallDto> bookings, List<CommentResponseDto> commentsResponseDto);

    List<ItemDto> search(Long userId, String text);

    void checkItemsAvailability(Long id);

    Item getItemByOwner(Long itemId);

    CommentResponseDto createComment(Long userId, Long itemId, String text);

    List<CommentResponseDto> getCommentList(Long itemId);

}
