package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto addNewItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isPresent()) {
            log.info("Пользователь с id" + userId + "успешно найден");
            itemRequestDto.setCreated(LocalDateTime.now());
            ItemRequest itemRequest = ItemRequestMapper.toDtoItemRequest(itemRequestDto, requester.get());
            itemRequest.setRequester(requester.get());
            itemRequest = itemRequestRepository.save(itemRequest);
            log.info("Пользователь с id" + userId + "успешно сохранен");
            itemRequestDto.setId(itemRequest.getId());
            itemRequestDto.setRequesterId(itemRequest.getRequester().getId());
            return itemRequestDto;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Пользователь с id" + userId + "не найден в базе");
        }
    }

    @Override
    public List<ItemRequestDto> getUsersRequests(Long userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId, Integer size, Integer from) {
        return null;
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        return null;
    }
}
