package ru.practicum.shareit.exceptions;

import java.time.LocalDateTime;

public class ErrorBookingsBody {
    LocalDateTime timestamp;
    Integer status;
    String error;
    String path;

    public ErrorBookingsBody(LocalDateTime timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
