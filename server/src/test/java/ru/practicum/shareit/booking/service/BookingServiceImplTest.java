package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.enums.StatusDto;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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
    User userDima;
    Item itemElena;
    Item itemDima;
    Booking bookingByDaria;
    Long userElenaId;
    Long userDariaId;
    Long userDimaId;
    Long bookingByDariaId;
    Long itemElenaId;
    Long itemDimaId;
    PageRequest pageRequest = PageRequest.of(0, 10);

    @AfterAll()
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings", "comments", "items", "requests", "users");
    }

    @BeforeAll()
    void addDate() {
        userElena = new User("Elena", "chaldina.e@gmail.com");
        userRepository.save(userElena);
        userElenaId = userRepository.findAll().get(0).getId();

        userDaria = new User("Daria", "vilkova.e@gmail.com");
        userRepository.save(userDaria);
        userDariaId = userRepository.findAll().get(1).getId();

        userDima = new User("Dima", "bilunov.e@gmail.com");
        userRepository.save(userDima);
        userDimaId = userRepository.findAll().get(2).getId();

        itemElena = new Item("Elena'sItem", "desc", true, userElena);
        itemRepository.save(itemElena);
        itemElenaId = itemRepository.findAll().get(0).getId();

        itemDima = new Item("Dima'sItem", "desc", true, userDima);
        itemRepository.save(itemDima);
        itemDimaId = itemRepository.findAll().get(1).getId();

        bookingByDaria = new Booking(LocalDateTime.now().plusHours(1).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(1).withSecond(0).withNano(0), itemElena, userDaria);
        bookingRepository.save(bookingByDaria);
        bookingByDariaId = bookingRepository.findAll().get(0).getId();
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
        BookingDto expected = new BookingDto(LocalDateTime.now().plusHours(1).withSecond(0).withNano(0),
                LocalDateTime.now().minusDays(1).withSecond(0).withNano(0),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_startDateBeforeCurrent() {
        BookingDto expected = new BookingDto(LocalDateTime.now().minusHours(5).withSecond(0).withNano(0),
                LocalDateTime.now().minusDays(1).withSecond(0).withNano(0),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_startDateAfterEndDate() {
        BookingDto expected = new BookingDto(LocalDateTime.now().plusDays(1).withSecond(0).withNano(0),
                LocalDateTime.now().plusHours(1).withSecond(0).withNano(0),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userDaria), itemElena, expected));
    }

    @Test
    void addNewBooking_rentOwn() {
        BookingDto expected = new BookingDto(LocalDateTime.now().plusDays(1).withSecond(0).withNano(0),
                LocalDateTime.now().plusHours(1).withSecond(0).withNano(0),
                Status.WAITING, UserMapper.toUserDto(userDaria), ItemMapper.toItemDto(itemElena));
        assertThrows(RequestError.class, () -> bookingService.addNewBooking(UserMapper.toUserDto(userElena), itemElena, expected));
    }

    //status != Status.WAITING
    @Test
    void patch_throwIncorrectStatus() {
        bookingByDaria.setStatus(Status.APPROVED);
        bookingRepository.save(bookingByDaria);
        RequestError requestError = assertThrows(RequestError.class, () -> bookingService.patch(bookingByDariaId, userDariaId, true));
        assertEquals("400 BAD_REQUEST \"бронь уже обработана\"", requestError.getMessage().toString());
    }

    @Test
    void patch_throwIncorrectOwner() {
        bookingByDaria.setStatus(Status.WAITING);
        bookingRepository.save(bookingByDaria);
        RequestError requestError = assertThrows(RequestError.class, () -> bookingService.patch(bookingByDariaId, userDariaId, true));
        assertEquals("404 NOT_FOUND \"Пользователь не является владельцем вещи\"", requestError.getMessage().toString());
    }

    @Test
    void patch_correctCase_approved() {
        bookingByDaria.setStatus(Status.WAITING);
        bookingRepository.save(bookingByDaria);
        BookingDto expected = BookingMapper.toBookingDto(bookingByDaria);
        expected.setStatus(Status.APPROVED);
        BookingDto actual = bookingService.patch(bookingByDariaId, userElenaId, true);

        assertEquals(expected, actual);
    }

    @Test
    void patch_correctCase_rejected() {
        bookingByDaria.setStatus(Status.WAITING);
        bookingRepository.save(bookingByDaria);
        BookingDto expected = BookingMapper.toBookingDto(bookingByDaria);
        expected.setStatus(Status.REJECTED);
        BookingDto actual = bookingService.patch(bookingByDariaId, userElenaId, false);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingById_correctCase() {
        BookingDto actual = bookingService.getBookingById(bookingByDariaId, userElenaId);
        Status expectedStatus = actual.getStatus();
        BookingDto expected = BookingMapper.toBookingDto(bookingByDaria);
        expected.setStatus(expectedStatus);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingById_incorrectBooking() {
        RequestError requestError = assertThrows(RequestError.class, () -> bookingService.getBookingById(99L, userElenaId));
        assertEquals("404 NOT_FOUND \"Такая бронь не найдена\"", requestError.getMessage().toString());
    }

    @Test
    void getBookingById_incorrectUser() {
        RequestError requestError = assertThrows(RequestError.class, () -> bookingService.getBookingById(bookingByDariaId, userDimaId));
        assertEquals("404 NOT_FOUND \"Нет доступа на просмотр\"", requestError.getMessage().toString());
    }

    @Test
    void getBookingsByUserAndStateAll() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByUserAndState(userElena, StatusDto.ALL, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByUserAndStateCURRENT() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByUserAndState(userElena, StatusDto.CURRENT, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByUserAndStatePAST() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByUserAndState(userElena, StatusDto.PAST, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByUserAndStateFUTURE() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByUserAndState(userElena, StatusDto.FUTURE, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByUserAndStateWAITINGOrREJECTED() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByUserAndState(userElena, StatusDto.WAITING, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByOwnerAndStateAll() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByOwnerAndState(userDaria, StatusDto.ALL, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByOwnerAndStateCURRENT() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByOwnerAndState(userDaria, StatusDto.CURRENT, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByOwnerAndStatePAST() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByOwnerAndState(userDaria, StatusDto.PAST, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByOwnerAndStateFUTURE() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByOwnerAndState(userDaria, StatusDto.FUTURE, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByOwnerAndStateWAITINGOrREJECTED() {
        List<BookingDto> expected = List.of();
        List<BookingDto> actual = bookingService.getBookingsByOwnerAndState(userDaria, StatusDto.WAITING, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByItem() {
        List<BookingSmallDto> expected = List.of();
        List<BookingSmallDto> actual = bookingService.getBookingsByItem(itemDimaId);

        assertEquals(expected, actual);
    }

    @Test
    void getBookingsByOwner() {
        List<BookingSmallDto> expected = List.of();
        UserDto userDimaDto = UserMapper.toUserDto(userDima);
        List<BookingSmallDto> actual = bookingService.getBookingsByOwner(userDimaDto);

        assertEquals(expected, actual);
    }
}