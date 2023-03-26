package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    Long id;
    String name;
    @Email(message = "Невалидная почта")
    @NotBlank(message = "Почта не может быть пустой")
    String email;
}
