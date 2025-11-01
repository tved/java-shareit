package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, CreateItemRequest request);

    ItemDto updateItem(Long itemId, UpdateItemRequest request, Long userId);

    List<ItemDto> getItemsByUser(Long userId);

    List<ItemDto> search(String text);

    ItemDto getItemById(Long itemId);
}
