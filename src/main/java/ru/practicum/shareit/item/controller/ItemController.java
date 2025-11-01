package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @Valid @RequestBody CreateItemRequest request
    ) {
        return itemService.createItem(userId, request);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateItemRequest request
    ) {
        return itemService.updateItem(itemId, request, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        return itemService.search(text);
    }
}
