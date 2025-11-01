package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class CreateItemRequest {
    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Available is required")
    Boolean available;
}
