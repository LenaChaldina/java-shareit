package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

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
    ItemRequest itemRequest;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterAll()
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings", "comments", "items", "requests", "users");
    }

    @Test
    void addNewItem_withoutRequestId() {
        User user1 = userRepository.save(user);
        UserDto userDto1 = UserMapper.toUserDto(user1);
        ItemDto actualItemDto = itemService.addNewItem(userDto1, itemDto, null);
        assertEquals(itemDto, actualItemDto);
    }

   /* @Test
    void addNewItem_withRequestId() {
        itemRequest = new ItemRequest("reqDesc", userRepository.findAll().get(0),
                LocalDateTime.of(1992, 7, 7, 2, 30), null);
        itemRequestRepository.save(itemRequest);
        ItemDto actualItemDto = itemService.addNewItem(userDto, itemDto, 1L);
        assertEquals(itemDtoReq, actualItemDto);
    }*/

   /* @Test
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
    }*/

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

    @Test
    void createComment_emptyUser() {
        assertThrows(RequestError.class, () -> itemService.createComment(10L, 1L, "comment"));
    }

    @Test
    void createComment_emptyItem() {
        assertThrows(RequestError.class, () -> itemService.createComment(1L, 10L, "comment"));
    }

    @Test
    void createComment_noBooker() {
        assertThrows(RequestError.class, () -> itemService.createComment(1L, 1L, "comment"));
    }

    //пользователь не закончил аренду
    /*@Test
    void createComment_afterBooking() {
        Booking bookingIncorrect = new Booking(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1), item, user);
        bookingRepository.save(bookingIncorrect);
        assertThrows(RequestError.class, () -> itemService.createComment(1L, 1L, "comment"));
    }*/

    /* @Test
     void createComment_ok() {
         User userDaria = new User("Daria", "dar@gmail.com");
         userRepository.save(userDaria);
         Item dariaItem = new Item(2L, "item2", "descc", true, userDaria);
         itemRepository.save(dariaItem);
         Booking bookingCorrect = new Booking(LocalDateTime.now().minusMonths(1), LocalDateTime.now().minusSeconds(1), item, userDaria);
         bookingCorrect.setStatus(Status.WAITING);
         bookingRepository.save(bookingCorrect);
         CommentResponseDto exp = new CommentResponseDto(1L, "commentNew", "Daria", LocalDateTime.now().withSecond(0).withNano(0));
         CommentResponseDto actual = itemService.createComment(user.getId(), item.getId(), "commentNew");
         actual.setCreated(LocalDateTime.now().withSecond(0).withNano(0));

         assertEquals(exp, actual);
     }
 */
    @Test
    void getCommentList() {
        List<CommentResponseDto> exp = List.of();
        List<CommentResponseDto> actual = itemService.getCommentList(item.getId());

        assertEquals(exp, actual);
    }
}