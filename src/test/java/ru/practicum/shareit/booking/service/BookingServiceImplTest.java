package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingServiceImplTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    User userElena;
    User userDaria;
    Item itemElena;
    Booking bookingByDaria;

    @AfterAll()
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings", "comments", "items", "requests", "users");
    }

    @BeforeAll()
    void addDate() {
        userElena = new User("Elena", "chaldina.e@gmail.com");
        userRepository.save(userElena);
        userDaria = new User("Daria", "vilkova.e@gmail.com");
        userRepository.save(userDaria);
        itemElena = new Item("Elena'sItem", "desc", true, userElena);
        itemRepository.save(itemElena);
        bookingByDaria = new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1), itemElena, userDaria);
        bookingRepository.save(bookingByDaria);
    }

    @Test
    void addNewBooking_correctCase() {
        BookingDto bookingDtoMock = new BookingDto(LocalDateTime.now().plusHours(1).withNano(0).withSecond(0),
                LocalDateTime.now().plusDays(1).withNano(0).withSecond(0),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        BookingDto actual = bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, bookingDtoMock);

        BookingDto expected = new BookingDto(actual.getId(), LocalDateTime.now().plusHours(1).withNano(0).withSecond(0),
                LocalDateTime.now().plusDays(1).withNano(0).withSecond(0),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));

        assertEquals(expected, actual);
    }

    @Test
    void addNewBooking_endDateBeforeCurrent() {
        BookingDto expected = new BookingDto(LocalDateTime.now().plusHours(1), LocalDateTime.now().minusDays(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_startDateBeforeCurrent() {
        BookingDto expected = new BookingDto(LocalDateTime.now().minusHours(5), LocalDateTime.now().minusDays(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_startDateAfterEndDate() {
        BookingDto expected = new BookingDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_rentOwn() {
        BookingDto expected = new BookingDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userElena), itemElena, expected));
    }

    @Test
    void patch() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookingsByUserAndState() {
    }

    @Test
    void getBookingsByOwnerAndState() {
    }

    @Test
    void getBookingsByItem() {
    }

    @Test
    void getBookingsByOwner() {
    }
}