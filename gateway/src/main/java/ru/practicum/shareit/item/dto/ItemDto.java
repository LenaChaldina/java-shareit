package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @NotEmpty(message = "Описание не может быть пустым")
    String description;
    //статус о том, доступна или нет вещь для аренды
    @BooleanFlag()
    @NotNull
    Boolean available;
    //владелец вещи
    @Transient
    Long ownerId;
    Long requestId;
    private Long lastBookingId;
    private Long nextBookingId;
}
