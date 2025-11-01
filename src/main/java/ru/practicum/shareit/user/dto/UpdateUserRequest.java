package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Value;

@Value
public class UpdateUserRequest {
    String name;

    @Email(message = "Email should be valid")
    String email;
}
