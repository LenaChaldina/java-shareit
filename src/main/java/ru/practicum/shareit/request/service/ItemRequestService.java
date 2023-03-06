package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getUsersRequests(Long userId);

    List<ItemRequestDto> getRequests(Long userId, Integer size, Integer from);

    ItemRequestDto getRequestById(Long requestId, Long userId);
}
