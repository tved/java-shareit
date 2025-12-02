package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static ItemWithDataDto toItemWithDataDto(Item item, BookingDates lastDates, BookingDates nextDates, List<CommentDto> comments) {
        return new ItemWithDataDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastDates,
                nextDates,
                comments
        );
    }

    public static Item toItem(CreateItemRequest request, Long userId) {
        User owner = User.builder().id(userId).build();

        return Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .owner(owner)
                .build();
    }

    public static Item updateItem(Item existing, UpdateItemRequest request) {
        return existing.toBuilder()
                .name(request.getName() != null ? request.getName() : existing.getName())
                .description(request.getDescription() != null ? request.getDescription() : existing.getDescription())
                .available(request.getAvailable() != null ? request.getAvailable() : existing.getAvailable())
                .build();
    }
}
