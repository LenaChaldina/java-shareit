package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    private ItemRequestService itemRequestService;
    long requestId = 1L;
    long userId = 1L;
    List<ItemRequestDto> itemRequestDtos = List.of();
    PageRequest pageRequest = PageRequest.of(0, 10);
    ItemRequestDto itemRequestDto = new ItemRequestDto(requestId, "desc", userId, LocalDateTime.now().withNano(0), null);

    @SneakyThrows
    @Test
    void add() {
        when(itemRequestService.addNewItemRequest(userId, itemRequestDto)).thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests", userId, itemRequestDto)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void getRequests() {
        when(itemRequestService.getRequests(userId, pageRequest)).thenReturn(itemRequestDtos);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("size", "10")
                        .param("from", "0"))
                .andExpect(status().isOk());

        verify(itemRequestService).getRequests(userId, pageRequest);
    }

    @SneakyThrows
    @Test
    void getUsersRequests() {
        when(itemRequestService.getUsersRequests(userId)).thenReturn(itemRequestDtos);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemRequestService).getUsersRequests(userId);
    }

    @SneakyThrows
    @Test
    void getRequestById() {
        when(itemRequestService.addNewItemRequest(userId, itemRequestDto)).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/{requestId}", requestId, userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemRequestService).getRequestById(requestId, userId);
    }
}
