package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDtoForUpdate {
    Long id;
    String name;
    @Email(message = "Невалидная почта")
    String email;
}
