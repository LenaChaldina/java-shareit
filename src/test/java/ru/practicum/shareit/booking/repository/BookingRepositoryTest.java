package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    Long userIdElena = 1L;
    Long itemId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 10);
    User userInputElena = new User(userIdElena, "Elena", "chaldina.e@gmail.com");
    Item item = new Item(itemId, "name", "desc", true, userInputElena);
    Booking booking = new Booking(LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0).plusDays(1),
            item, userInputElena, Status.WAITING, null);
    Booking pastBooking = new Booking( LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(2),
            item, userInputElena, Status.WAITING, null);
    Booking futureBooking = new Booking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
            item, userInputElena, Status.WAITING, null);

    @BeforeAll
    private void addBooking() {
        userRepository.save(userInputElena);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(pastBooking);
        bookingRepository.save(futureBooking);
    }

    @Test
    void findAllByBookerOrderByStartDesc() {
        Page<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDesc(userInputElena, pageRequest);
        assertEquals(3, bookings.getContent().size());
        assertEquals(3, bookings.getContent().get(0).getId());
    }

    //current
    @Test
    void findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc() {
        Page<Booking> bookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(userInputElena, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getId());
    }

    //past
    @Test
    void findAllByBookerAndEndBeforeOrderByStartDesc() {
        Page<Booking> bookings = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(userInputElena, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(2, bookings.getContent().get(0).getId());
    }

    //FUTURE
    @Test
    void findAllByBookerAndStartAfterOrderByStartDesc() {
        Page<Booking> bookings = bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(userInputElena, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(3, bookings.getContent().get(0).getId());
    }

    //Status.WAITING
    @Test
    void findAllByBookerAndStatusEquals() {
        Page<Booking> bookings = bookingRepository.findAllByBookerAndStatusEquals(userInputElena, Status.WAITING, pageRequest);
        assertEquals(3, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getId());
    }

    @Test
    void getBookingsByOwnerId() {
        Page<Booking> bookings = bookingRepository.getBookingsByOwnerId(userIdElena, pageRequest);
        assertEquals(3, bookings.getContent().size());
        assertEquals(3, bookings.getContent().get(0).getId());
    }

    @Test
    void getCurrentBookingByOwnerId() {
        Page<Booking> bookings = bookingRepository.getCurrentBookingByOwnerId(userIdElena, pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getId());
    }

    @Test
    void getFutureBookingByOwnerId() {
        Page<Booking> bookings = bookingRepository.getFutureBookingByOwnerId(userIdElena, pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(3, bookings.getContent().get(0).getId());
    }

    @Test
    void getPastBookingByOwnerId() {
        Page<Booking> bookings = bookingRepository.getPastBookingByOwnerId(userIdElena, pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(2, bookings.getContent().get(0).getId());
    }

    @Test
    void getStateBookingByOwnerId() {
        Page<Booking> bookings = bookingRepository.getStateBookingByOwnerId(userIdElena, Status.WAITING, pageRequest);
        assertEquals(3, bookings.getContent().size());
    }

    @Test
    void getBookingsByItem() {
        List<Booking> bookings = bookingRepository.getBookingsByItem(itemId);
        assertEquals(3, bookings.size());
    }

    @Test
    void getBookingsByOwner() {
        List<Booking> bookings = bookingRepository.getBookingsByOwner(userIdElena);
        assertEquals(3, bookings.size());
    }

    @Test
    void getBookingsByItemOrderByStartAsc() {
        List<Booking> bookings = bookingRepository.getBookingsByItemOrderByStartAsc(itemId);
        assertEquals(3, bookings.size());
    }

    @Test
    void getBookingsByBookerIdAndItem() {
        List<Booking> bookings = bookingRepository.getBookingsByBookerIdAndItem(userIdElena, itemId);
        assertEquals(3, bookings.size());
    }

    @AfterAll
    private void delete() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}