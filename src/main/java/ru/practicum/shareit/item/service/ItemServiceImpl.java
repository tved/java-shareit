package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository storage;
    private final UserService userService;

    @Override
    public ItemDto createItem(Long userId, CreateItemRequest request) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        Item newItem = ItemMapper.toItem(request, userId);
        newItem = storage.create(newItem);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long id, UpdateItemRequest request, Long userId) {
        Item existing = storage.get(id)
                .orElseThrow(() -> new NotFoundException("Item with id=" + id + " not found"));
        if (!existing.getOwner().equals(userId)) {
            throw new ForbiddenException("User with id=" + userId + " is not allows to modify this item");
        }
        Item updated = ItemMapper.updateItem(existing, request);
        storage.update(updated);
        return ItemMapper.toItemDto(updated);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = storage.get(id)
                .orElseThrow(() -> new NotFoundException("Item with id=" + id + " not found"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        return storage.getItemsByUser(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        return storage.search(text).stream().map(ItemMapper::toItemDto).toList();
    }
}
