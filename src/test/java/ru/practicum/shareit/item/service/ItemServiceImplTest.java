package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;
    ItemRequest itemRequest;
    UserDto userLenaDto;
    ItemDto itemByLenaDto;
    ItemDto itemDtoReq;
    User userLena;
    Long userLenaId;
    Item itemByLena;
    Long itemByLenaId;
    Long requestId;
    ItemRequestDto itemRequestDto;
    private User userDima;
    Long userDimaId;
    private UserDto userDimaDto;
    PageRequest pageRequest = PageRequest.of(0, 10);

    @BeforeAll
    void add() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings", "comments", "items", "requests", "users");
        userLena = new User("Lena", "chaldina.e@glail.com");
        userRepository.save(userLena);
        userLenaId = userRepository.findAll().get(0).getId();
        userLenaDto = new UserDto(userLenaId, "Lena", "chaldina.e@glail.com");

        userDima = new User("Dima", "bilunov.e@glail.com");
        userRepository.save(userDima);
        userDimaId = userRepository.findAll().get(1).getId();
        userDimaDto = new UserDto(userDimaId, "Lena", "chaldina.e@glail.com");

        itemByLena = new Item("item", "desc", true, userLena);
        itemRepository.save(itemByLena);
        itemByLenaId = itemRepository.findAll().get(0).getId();
        itemByLenaDto = new ItemDto(itemByLenaId, "item", "desc", true, null);

        List<Item> items = List.of();
        itemRequest = new ItemRequest("reqDesc", userRepository.findAll().get(0),
                LocalDateTime.of(1992, 7, 7, 2, 30), items);
        itemRequestRepository.save(itemRequest);
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        requestId = itemRequestRepository.findAll().get(0).getId();

        itemDtoReq = new ItemDto(itemByLenaId, "item", "desc", true, requestId);
    }

    @AfterAll()
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings", "comments", "items", "requests", "users");
    }

    @Test
    void addNewItem_withoutRequestId() {
        ItemDto actualItemDto = itemService.addNewItem(userLenaDto, itemByLenaDto, null);
        assertEquals(itemByLenaDto, actualItemDto);
    }

    @Test
    void addNewItem_withRequestId() {
        ItemDto actualItemDto = itemService.addNewItem(userLenaDto, itemByLenaDto, requestId);
        assertEquals(itemDtoReq, actualItemDto);
    }

    @Test
    void putItem() {
        ItemDto actualItemDto = itemService.putItem(itemByLenaId, itemDtoReq, userLenaId);
        assertEquals(itemDtoReq, actualItemDto);
    }

    @Test
    void checkItemsAvailability() {
        itemByLena.setAvailable(false);
        itemRepository.save(itemByLena);
        assertThrows(RequestError.class, () -> itemService.checkItemsAvailability(itemByLenaId));
    }

    @Test
    void getItemById() {
        itemByLena.setAvailable(true);
        itemRepository.save(itemByLena);
        List<CommentResponseDto> commentResponseDtos = List.of();
        ItemDtoForBooking expItemDtoForBooking = new ItemDtoForBooking(itemByLenaId, "item", "desc", true);
        expItemDtoForBooking.setComments(commentResponseDtos);
        ItemDtoForBooking actualItemDtoForBooking = itemService.getItemById(itemByLenaId, userLena, null);
        assertEquals(expItemDtoForBooking, actualItemDtoForBooking);
    }

    @Test
    void getItemByOwner_incorrectItemId() {
        assertThrows(RequestError.class, () -> itemService.getItemByOwner(99L));
    }

    @Test
    void getItemsByUser() {
        List<ItemDtoForBooking> itemDtoForBookings = List.of();
        List<BookingSmallDto> bookings = List.of();
        List<CommentResponseDto> commentsResponseDto = List.of();
        List<ItemDtoForBooking> actual = itemService.getItemsByUser(userDimaDto, bookings, pageRequest);
        assertEquals(itemDtoForBookings, actual);
    }

    @Test
    void search_textIsBlank() {
        List<ItemDto> exp = new ArrayList<>();
        List<ItemDto> actual = itemService.search(userLenaId, "", pageRequest);

        assertEquals(exp, actual);
    }

    @Test
    void search_textIsNotBlank() {
        List<ItemDto> exp = List.of();
        List<ItemDto> actual = itemService.search(userLenaId, "text", pageRequest);

        assertEquals(exp, actual);
    }

    @Test
    void createComment_empty() {
        assertThrows(RequestError.class, () -> itemService.createComment(userLenaId, itemByLenaId, ""));
    }

    @Test
    void createComment_emptyUser() {
        assertThrows(RequestError.class, () -> itemService.createComment(99L, itemByLenaId, "comment"));
    }

    @Test
    void createComment_emptyItem() {
        assertThrows(RequestError.class, () -> itemService.createComment(userLenaId, 99L, "comment"));
    }

    @Test
    void createComment_noBooker() {
        assertThrows(RequestError.class, () -> itemService.createComment(userLenaId, itemByLenaId, "comment"));
    }

    //пользователь не закончил аренду
    @Test
    void createComment_afterBooking() {
        Booking bookingIncorrect = new Booking(LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1), itemByLena, userLena);
        bookingRepository.save(bookingIncorrect);
        assertThrows(RequestError.class, () -> itemService.createComment(userLenaId, itemByLenaId, "comment"));
    }

    @Test
     void createComment_ok() {
        User userDaria = new User("Daria", "dar@gmail.com");
        userRepository.save(userDaria);
        Item dariaItem = new Item("item2", "descc", true, userDaria);
        itemRepository.save(dariaItem);
        Booking bookingCorrect = new Booking(LocalDateTime.now().minusMonths(1), LocalDateTime.now().minusSeconds(1), dariaItem, userDaria);
        bookingCorrect.setStatus(Status.WAITING);
        bookingRepository.save(bookingCorrect);
        CommentResponseDto actual = itemService.createComment(userDaria.getId(), dariaItem.getId(), "commentNew");
        CommentResponseDto exp = new CommentResponseDto(actual.getId(), "commentNew", "Daria", LocalDateTime.now().withSecond(0).withNano(0));
        actual.setCreated(LocalDateTime.now().withSecond(0).withNano(0));

        assertEquals(exp, actual);
    }

    @Test
    void getCommentList() {
        List<CommentResponseDto> exp = List.of();
        List<CommentResponseDto> actual = itemService.getCommentList(itemByLena.getId());

        assertEquals(exp, actual);
    }
}