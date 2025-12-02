package ru.practicum.shareit.item.dto;

import java.time.Instant;

public record CommentDto(
    Long id,
    String text,
    Long itemId,
    String authorName,
    Instant created
) {}
