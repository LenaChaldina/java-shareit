package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceImplTest {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BookingRepository bookingRepository;
    UserDto userDto = new UserDto(1L, "Lena", "chaldina.e@glail.com");
    ItemDto itemDto = new ItemDto(1L, "item", "desc", true, null);
    ItemDto itemDtoReq = new ItemDto(1L, "item", "desc", true, 1L);
    User user = new User(1L, "Lena", "chaldina.e@glail.com");
    Item item = new Item(1L, "item", "desc", true, user);
    ItemRequest itemRequest = new ItemRequest(1L, "reqDesc", user,
            LocalDateTime.of(1992, 7, 7, 2, 30), null);
    List<Item> items = List.of();
    List<ItemRequestDto> itemRequestDtos = List.of();
    List<ItemRequest> itemRequests = List.of();
    List<ItemDtoForRequest> itemDtoForRequests = List.of();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterAll()
    void tearDown() {
        String sql = "DROP TABLE IF EXISTS public.requests CASCADE;\n" +
                "DROP TABLE IF EXISTS public.comments CASCADE;\n" +
                "DROP TABLE IF EXISTS public.bookings CASCADE;\n" +
                "DROP TABLE IF EXISTS public.items CASCADE;\n" +
                "DROP TABLE IF EXISTS public.users CASCADE; ";
        jdbcTemplate.update(sql);
    }

    @Test
    void addNewItem_withoutRequestId() {
        userRepository.save(user);
        ItemDto actualItemDto = itemService.addNewItem(userDto, itemDto, null);
        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void addNewItem_withRequestId() {
        itemRequestRepository.save(itemRequest);
        ItemDto actualItemDto = itemService.addNewItem(userDto, itemDto, 1L);
        assertEquals(itemDtoReq, actualItemDto);
    }

    @Test
    void putItem() {
        ItemDto itemDtoForChange = new ItemDto(1L, "itemNew", "descNew", false, 1L);
        ItemDto actualItemDto = itemService.putItem(1L, itemDtoForChange, 1L);
        assertEquals(itemDtoForChange, actualItemDto);
    }

    @Test
    void checkItemsAvailability() {
        assertThrows(RequestError.class, () -> itemService.checkItemsAvailability(2L));
    }

    @Test
    void getItemById() {
        List<CommentResponseDto> commentResponseDtos = List.of();
        ItemDtoForBooking expItemDtoForBooking = new ItemDtoForBooking(1L, "item", "desc", true);
        expItemDtoForBooking.setComments(commentResponseDtos);
        ItemDtoForBooking actualItemDtoForBooking = itemService.getItemById(1L, user, null, null);
        assertEquals(expItemDtoForBooking, actualItemDtoForBooking);
    }

    @Test
    void getItemByOwner_incorrectItemId() {
        assertThrows(RequestError.class, () -> itemService.getItemByOwner(2L));
    }

    @Test
    void getItemsByUser() {
        List<ItemDtoForBooking> itemDtoForBookings = List.of();
        List<BookingSmallDto> bookings = List.of();
        List<CommentResponseDto> commentsResponseDto = List.of();
        List<ItemDtoForBooking> actual = itemService.getItemsByUser(userDto, bookings, commentsResponseDto);
        assertEquals(itemDtoForBookings, actual);
    }

    @Test
    void search_textIsBlank() {
        List<ItemDto> exp = new ArrayList<>();
        List<ItemDto> actual = itemService.search(1L, "");

        assertEquals(exp, actual);
    }

    @Test
    void search_textIsNotBlank() {
        List<ItemDto> exp = List.of();
        List<ItemDto> actual = itemService.search(1L, "text");

        assertEquals(exp, actual);
    }

    @Test
    void createComment_empty() {
        assertThrows(RequestError.class, () -> itemService.createComment(1L, 1L, ""));
    }
}