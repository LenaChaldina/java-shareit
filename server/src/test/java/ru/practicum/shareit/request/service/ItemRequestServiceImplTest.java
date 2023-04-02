package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    Long userId = 1L;
    Long requestId = 1L;
    User userInput = new User(userId, "Elena", "chaldina.e@gmail.com");
    List<Item> items = List.of();
    List<ItemRequestDto> itemRequestDtos = List.of();
    List<ItemRequest> itemRequests = List.of();
    List<ItemDtoForRequest> itemDtoForRequests = List.of();

   @Test
    void addNewItemRequest_valid() {
        ItemRequestDto itemRequestDtoInput = new ItemRequestDto(1L, "Хотел бы воспользоваться щёткой для обуви", null, null, null);
        ItemRequestDto itemRequestDto = new ItemRequestDto(requestId, "Хотел бы воспользоваться щёткой для обуви", userId, LocalDateTime.now().withSecond(0).withNano(0), null);
        ItemRequest itemRequest = new ItemRequest(1L, "Хотел бы воспользоваться щёткой для обуви", userInput, LocalDateTime.now().withNano(0), items);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userInput));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequestDto actualItemRequestDto = itemRequestService.addNewItemRequest(userId, itemRequestDtoInput);
        actualItemRequestDto.setCreated(LocalDateTime.now().withSecond(0).withNano(0));

        assertEquals(itemRequestDto, actualItemRequestDto);
        verify(itemRequestRepository).save(itemRequest);
    }

    @Test
    void getUsersRequests() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userInput));
        when(itemRequestRepository.findRequestsByUser(userId)).thenReturn(itemRequests);

        List<ItemRequestDto> actualItemRequestDtos = itemRequestService.getUsersRequests(userId);
        assertEquals(itemRequestDtos, actualItemRequestDtos);
    }

    @Test
    void getRequests() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userInput));
        when(itemRequestRepository.findRequestsWithoutOwner(userId, pageRequest)).thenReturn(Page.empty());

        List<ItemRequestDto> actualRequestDto = itemRequestService.getRequests(userId, pageRequest);

        assertEquals(itemRequestDtos, actualRequestDto);
    }

    @Test
    void getRequestById_whenRequestFound_thenReturnedRequest() {
        ItemRequestDto expectedRequestDto = new ItemRequestDto(requestId, "Request", requestId, LocalDateTime.of(2022,
                Month.FEBRUARY, 24, 04, 00, 00), itemDtoForRequests);
        ItemRequest itemRequestInput = new ItemRequest(requestId, "Request", userInput, LocalDateTime.of(2022,
                Month.FEBRUARY, 24, 04, 00, 00), items);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userInput));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequestInput));

        ItemRequestDto actualRequestDto = itemRequestService.getRequestById(requestId, userId);

        assertEquals(expectedRequestDto, actualRequestDto);
    }

    @Test
    void getRequestById_whenRequestNotFound_thenRequestNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userInput));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(RequestError.class, () -> itemRequestService.getRequestById(requestId, userId));
    }
}