package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingSmallDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.RequestError;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto addNewItem(UserDto userDto, ItemDto itemDto) {
        User user = UserMapper.dtoToUser(userDto);
        Item item = ItemMapper.dtoToItem(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        log.info("Вещь с ID:" + item.getId() + " добавлена пользователем с id:" + user.getId());
        return ItemMapper.toItemDto(item);
    }

    //Редактирование вещи.
    // Изменить можно название, описание и статус доступа к аренде.
    // Редактировать вещь может только её владелец.
    @Override
    public ItemDto putItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = ItemMapper.dtoToItem(itemDto);
        checkItemId(itemId);
        checkUserId(itemId, userId);
        Optional<Item> ItemFromDbe = itemRepository.findById(itemId);
        if (item.getName() != null) {
            ItemFromDbe.get().setName(item.getName());
        }
        if (item.getDescription() != null) {
            ItemFromDbe.get().setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            ItemFromDbe.get().setAvailable(item.getAvailable());
        }
        itemRepository.save(ItemFromDbe.get());
        log.info("Вещь с ID:" + itemId + " обновлена пользователем с id:" + userId);
        return ItemMapper.toItemDto(ItemFromDbe.get());
    }

    private Boolean checkItemId(Long itemId) {
        Optional<Item> ItemFromDbe = itemRepository.findById(itemId);
        if (ItemFromDbe.isPresent()) {
            return true;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Вещи c id:" + itemId + " нет в списке");
        }
    }

    public void checkItemsAvailability(Long itemId) {
        checkItemId(itemId);
        Optional<Item> ItemFromDbe = itemRepository.findById(itemId);
        if (!ItemFromDbe.get().getAvailable()) {
            throw new RequestError(HttpStatus.BAD_REQUEST, "Вещь c id:" + ItemFromDbe.get().getId() + " недоступна");
        }
    }

    private Boolean checkUserId(Long itemId, Long userId) {
        Optional<Item> ItemFromDbe = itemRepository.findById(itemId);
        if (ItemFromDbe.get().getOwner().getId().equals(userId)) {
            return true;
        } else {
            throw new RequestError(HttpStatus.NOT_FOUND, "Редактировать вещь может только её владелец");
        }
    }

    //Просмотр информации о конкретной вещи по её идентификатору.
    //Информацию о вещи может просмотреть любой пользователь.
    @Override
    public ItemDtoForBooking getItemById(Long itemId, User user, List<BookingSmallDto> bookingSmallDto) {
        checkItemId(itemId);
        Item item = itemRepository.findById(itemId).get();
        log.info("Вещь с ID:" + itemId + " успешно найдена");
        return ItemMapper.toItemDtoForBooking(item, bookingSmallDto);
    }
    @Override
    public Item getItemByOwner(Long itemId) {
        checkItemId(itemId);
        Item item = itemRepository.findById(itemId).get();
        return item;
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
    @Override
    public List<ItemDtoForBooking> getItemsByUser(UserDto userDto, List<BookingSmallDto> bookings) {
        User user = UserMapper.dtoToUser(userDto);
        List<Item> itemsForOwner = new ArrayList<>();
        itemsForOwner = itemRepository.findByOwnerOrderById(user);
        log.info("Найдены все вещи пользователя с id:" + user.getId());
        return itemsForOwner
                .stream()
                .map(item -> ItemMapper.toItemDtoForBooking(item, bookings))
                .collect(Collectors.toList());
    }

    //Поиск вещи потенциальным арендатором.
    //Пользователь передаёт в строке запроса текст, и система ищет вещи,
    //содержащие этот текст в названии или описании.
    //Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.
    //Проверьте, что поиск возвращает только доступные для аренды вещи.
    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository
                    .search(text)
                    .stream()
                    .map(item -> ItemMapper.toItemDto(item))
                    .collect(Collectors.toList());
        }
    }
}
