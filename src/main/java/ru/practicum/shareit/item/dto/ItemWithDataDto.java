package ru.practicum.shareit.item.dto;

import java.util.List;

public record ItemWithDataDto(
     Long id,
     String name,
     String description,
     Boolean available,
     BookingDates lastBooking,
     BookingDates nextBooking,
     List<CommentDto> comments
) {}