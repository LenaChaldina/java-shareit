package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.enums.StatusDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    PageRequest pageRequest = PageRequest.of(0, 10);

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
        bookingDto.setItemId(itemId);
        when(bookingService.patch(bookingId, userId, true)).thenReturn(bookingDto);
        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
        verify(bookingService).patch(bookingId, userId, true);
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingService).getBookingById(requestId, userId);
    }

    @SneakyThrows
    @Test
    void getBookingsByUser() {
        List<BookingDto> bookingDtos = List.of();
        when(userService.findUserById(userId)).thenReturn(userDto);
        when(bookingService.getBookingsByUserAndState(user, StatusDto.REJECTED, pageRequest)).thenReturn(bookingDtos);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "REJECTED")
                        .param("size", "10")
                        .param("from", "0"))
                .andExpect(status().isOk());

        verify(bookingService).getBookingsByUserAndState(user, StatusDto.REJECTED, pageRequest);
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner() {
        List<BookingDto> bookingDtos = List.of();

        when(userService.findUserById(userId)).thenReturn(userDto);
        when(bookingService.getBookingsByOwnerAndState(user, StatusDto.REJECTED, pageRequest)).thenReturn(bookingDtos);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "REJECTED")
                        .param("size", "10")
                        .param("from", "0"))
                .andExpect(status().isOk());

        verify(bookingService).getBookingsByOwnerAndState(user, StatusDto.REJECTED, pageRequest);
    }
}