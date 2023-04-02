package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    long userId = 1L;
    User user = new User(userId, "Elena", "chaldina.e@gmail.com");
    UserDto userDto = new UserDto(userId, "Elena", "chaldina.e@gmail.com");
    List<User> users = List.of();
    List<UserDto> userDtos = List.of();

    @SneakyThrows
    @Test
    void addUser() {
        when(userService.addUser(user)).thenReturn(userDto);
        String result = mockMvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void patchUser() {
        when(userService.patchUser(user, userId)).thenReturn(userDto);
        String result = mockMvc.perform(patch("/users/{id}", userId, userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void getUsers() {
        when(userService.getUsers()).thenReturn(userDtos);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getUsers();
    }

    @SneakyThrows
    @Test
    void getUser() {
        when(userService.findUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).findUserById(userId);
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userService).deleteUser(userId);
    }
}