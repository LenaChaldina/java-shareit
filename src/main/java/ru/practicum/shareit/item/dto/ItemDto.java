package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Transient;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
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
    User owner;
    //если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос
    Long requestId;
    private Booking lastBooking;
    private Booking nextBooking;


    public ItemDto(Long id, String name, String description, Boolean available, User owner, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        //this.owner = owner;
        this.requestId = requestId;
    }
}
