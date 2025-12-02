package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, CreateItemRequest request);

    ItemDto updateItem(Long itemId, UpdateItemRequest request, Long userId);

    List<ItemWithDataDto> getItemsByUser(Long userId);

    List<ItemDto> search(String text);

    ItemWithDataDto getItemById(Long itemId);

    Item getEntityById(Long itemId);

    CommentDto addComment(Long userId, Long itemId, NewCommentRequest text);
}
