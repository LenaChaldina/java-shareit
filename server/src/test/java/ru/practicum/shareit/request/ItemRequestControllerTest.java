package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void add() {
        ItemRequestDto itemRequestDtoInput = new ItemRequestDto("Хотел бы воспользоваться щёткой для обуви");
        ItemRequestDto itemRequestDtoOutput = new ItemRequestDto(1L, "Хотел бы воспользоваться щёткой для обуви", 1L, null, null);
        when(itemRequestService.addNewItemRequest(1L, itemRequestDtoInput)).thenReturn(itemRequestDtoOutput);
        ResponseEntity<ItemRequestDto> actualItemRequestDto = itemRequestController.add(1L, itemRequestDtoInput);

        assertEquals(HttpStatus.OK, actualItemRequestDto.getStatusCode());
        assertEquals(itemRequestDtoOutput, actualItemRequestDto.getBody());
    }

    @Test
    void getUsersRequests() {
        List<ItemRequestDto> itemRequestDtoOutputs = List.of(new ItemRequestDto());
        Mockito.when(itemRequestService.getUsersRequests(1L)).thenReturn(itemRequestDtoOutputs);

        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.getUsersRequests(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemRequestDtoOutputs, response.getBody());
    }

    @Test
    void getRequests() {
        List<ItemRequestDto> itemRequestDtoOutputs = List.of(new ItemRequestDto());
        PageRequest pageable = PageRequest.of(0, 10);
        Mockito.when(itemRequestService.getRequests(1L, pageable)).thenReturn(itemRequestDtoOutputs);

        List<ItemRequestDto> response = itemRequestController.getRequests(1L, 10, 0);

        assertEquals(itemRequestDtoOutputs, response);
    }

    @Test
    void getRequestById() {
        ItemRequestDto itemRequestDtoInput = new ItemRequestDto();
        Mockito.when(itemRequestService.getRequestById(1L, 1L)).thenReturn(itemRequestDtoInput);

        ItemRequestDto response = itemRequestController.getRequestById(1L, 1L);

        assertEquals(itemRequestDtoInput, response);
    }
}