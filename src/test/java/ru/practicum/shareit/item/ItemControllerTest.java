package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Comment;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
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
    Long requestId = 1L;
    User user = new User(userId, "Lena", "chaldina.e@gmail.com");
    UserDto userDto = new UserDto(userId, "Lena", "chaldina.e@gmail.com");
    Item item = new Item(itemId, "name", "desc", true, user);
    ItemDtoForBooking itemDtoForBooking = new ItemDtoForBooking(itemId, "name", "desc", true);
    ItemDto itemDto = new ItemDto(itemId, "name", "desc", true, requestId);
    List<ItemDto> itemDtos = List.of();
    Comment comment = new Comment(1L, "text", 1L, 1L, LocalDateTime.now().withNano(0));
    CommentResponseDto commentResponseDto = new CommentResponseDto(1L, "text", "name", LocalDateTime.now().withNano(0));
    BookingSmallDto bookingSmallDto = new BookingSmallDto(1L, 1L, 1L);
    List<CommentResponseDto> commentResponseDtos = List.of();
    List<BookingSmallDto> bookingSmallDtos = List.of();
    List<ItemDtoForBooking> itemDtoForBookings = List.of();


    @SneakyThrows
    @Test
    void add() {
        when(itemService.addNewItem(userDto, itemDto, itemDto.getRequestId())).thenReturn(itemDto);

        mockMvc.perform(post("/items", userId, itemDto)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void createComment() {
        when(itemService.createComment(userId, itemId, comment.getText())).thenReturn(commentResponseDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", userId, itemId, comment)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentResponseDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentResponseDto), result);
    }

    @SneakyThrows
    @Test
    void putItem() {
        when(itemService.putItem(itemId, itemDto, userId)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", userId, itemDto)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    /*@SneakyThrows
    @Test
    void getItemById() {
        when(itemService.getItemById(itemId, user, bookingSmallDtos, commentResponseDtos)).thenReturn(itemDtoForBooking);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }*/

    @SneakyThrows
    @Test
    void getItemsByUser() {
        when(itemService.getItemsByUser(userDto, bookingSmallDtos, commentResponseDtos)).thenReturn(itemDtoForBookings);

        mockMvc.perform(get("/items", itemId, userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void search() {
        when(itemService.search(userId, "")).thenReturn(itemDtos);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", ""))
                .andExpect(status().isOk());

        verify(itemService).search(userId, "");
    }
}