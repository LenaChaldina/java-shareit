package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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
        String sql =
                "DELETE FROM public.requests; \n" +
                        "ALTER SEQUENCE public.requests_request_id_seq RESTART WITH 1; \n" +
                        "DELETE FROM public.comments; \n" +
                        "ALTER SEQUENCE public.comments_comment_id_seq RESTART WITH 1; \n" +
                        "DELETE FROM public.bookings; \n" +
                        "ALTER SEQUENCE public.bookings_booking_id_seq RESTART WITH 1; \n" +
                        "DELETE FROM public.items; \n" +
                        "ALTER SEQUENCE public.items_item_id_seq RESTART WITH 1; \n" +
                        "DELETE FROM public.users; \n" +
                        "ALTER SEQUENCE public.users_user_id_seq RESTART WITH 1; ";
        jdbcTemplate.update(sql);
    }

    @BeforeAll()
    void addDate() {
        userElena = new User("Elena", "chaldina.e@gmail.com");
        userRepository.save(userElena);
        userDaria = new User("Daria", "vilkova.e@gmail.com");
        userRepository.save(userDaria);
        itemElena = new Item(1L, "Elena'sItem", "desc", true, userElena);
        itemRepository.save(itemElena);
        bookingByDaria = new Booking(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1), itemElena, userDaria);
        bookingRepository.save(bookingByDaria);
    }

    @Test
    void addNewBooking_correctCase() {
        BookingDto expected = new BookingDto(4L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        BookingDto actual = bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected);


        assertEquals(expected, actual);
    }

    @Test
    void addNewBooking_endDateBeforeCurrent() {
        BookingDto expected = new BookingDto(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().minusDays(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_startDateBeforeCurrent() {
        BookingDto expected = new BookingDto(1L, LocalDateTime.now().minusHours(5), LocalDateTime.now().minusDays(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_startDateAfterEndDate() {
        BookingDto expected = new BookingDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(1),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_rentOwn() {
        BookingDto expected = new BookingDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(1),
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