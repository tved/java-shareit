package ru.practicum.shareit.item.dto;

import lombok.Value;

@Value
public class UpdateItemRequest {
    String name;

    String description;

    Boolean available;
}
