package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestBody @Valid ItemRequestDto request) {
        return requestClient.add(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getUsersRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@PositiveOrZero
                                                         @RequestParam(value = "from", required = false) Integer from,
                                                         @Positive @RequestParam(value = "size", required = false) Integer size,
                                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        return requestClient.getRequestById(userId, requestId);
    }
}
