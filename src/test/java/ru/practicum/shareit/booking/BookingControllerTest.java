package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    ItemService itemService;
    @MockBean
    BookingService bookingService;
    Long userId = 1L;
    Long itemId = 1L;
    Long bookingId = 1L;
    Long requestId = 1L;
    User user = new User(userId, "Lena", "chaldina.e@gmail.com");
    UserDto userDto = new UserDto(userId, "Lena", "chaldina.e@gmail.com");
    Item item = new Item(itemId, "name", "desc", true, user);
    ItemDto itemDto = new ItemDto(itemId, "name", "desc", true, requestId);
    BookingDto bookingDto = new BookingDto(bookingId, LocalDateTime.now().withNano(0),
    LocalDateTime.now().withNano(0).plusDays(1), Status.WAITING, userDto, itemDto);
    BookingDto bookingDtoExp = new BookingDto();

    @SneakyThrows
    @Test
    void add() {
        when(bookingService.addNewBooking(userDto, item, bookingDto)).thenReturn(bookingDto);

         mockMvc.perform(post("/bookings", userId, bookingDto)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
    @SneakyThrows
    @Test
    void patch() {

    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookingsByUser() {
    }

    @Test
    void getBookingsByOwner() {
    }

    @Test
    void handle() {
    }
}