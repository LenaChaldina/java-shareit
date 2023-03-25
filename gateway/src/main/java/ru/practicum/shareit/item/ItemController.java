package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody @Valid ItemDto itemDto) {
        return itemClient.add(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId, @RequestBody CommentDto comment) {
        return itemClient.createComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> putItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody ItemDto itemDto,
                                          @PathVariable Integer itemId) {
        return itemClient.putItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@PositiveOrZero
                                                 @RequestParam(value = "from", required = false) Integer from,
                                                 @Positive
                                                 @RequestParam(value = "size", required = false) Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItemsByUser(from, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@PositiveOrZero
                                         @RequestParam(value = "from", required = false) Integer from,
                                         @Positive
                                         @RequestParam(value = "size", required = false) Integer size,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam String text) {
        return itemClient.search(from, size, userId, text);
    }
}