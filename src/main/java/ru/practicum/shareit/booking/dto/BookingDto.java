package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

public record BookingDto(
    Long id,
    LocalDateTime start,
    LocalDateTime end,
    ItemDto item,
    Status status,
    UserDto booker
){}
