package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, CreateItemRequest request) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        Item newItem = ItemMapper.toItem(request, userId);
        newItem = repository.save(newItem);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long id, UpdateItemRequest request, Long userId) {
        Item existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id=" + id + " not found"));
        if (!existing.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("User with id=" + userId + " is not allowed to modify this item");
        }
        Item updated = ItemMapper.updateItem(existing, request);
        repository.save(updated);
        return ItemMapper.toItemDto(updated);
    }

    @Override
    public ItemWithDataDto getItemById(Long id) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id=" + id + " not found"));
        List<CommentDto> comments = commentRepository.findByItem_Id(id).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        return ItemMapper.toItemWithDataDto(item, null, null, comments);
    }

    @Override
    public List<ItemWithDataDto> getItemsByUser(Long userId) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        LocalDateTime now = LocalDateTime.now();
        return repository.findByOwnerId(userId).stream().map(it -> {
            List<CommentDto> comments = commentRepository.findByItem_Id(it.getId())
                    .stream()
                    .map(CommentMapper::toCommentDto)
                    .toList();

            List<Booking> bookingsForItem = bookingRepository.findByItem_Id(it.getId());
            Booking last = bookingsForItem.stream()
                    .filter(b -> (b.getStartDate().isBefore(now) || b.getStartDate().isEqual(now)))
                    .filter(b -> b.getStatus() == Status.APPROVED)
                    .max(Comparator.comparing(Booking::getStartDate))
                    .orElse(null);
            Booking next = bookingsForItem.stream()
                    .filter(b -> b.getStartDate().isAfter(now))
                    .filter(b -> b.getStatus() == Status.APPROVED)
                    .min(Comparator.comparing(Booking::getStartDate))
                    .orElse(null);
            BookingDates lastDates = last != null
                    ? new BookingDates(last.getStartDate(), last.getEndDate())
                    : null;

            BookingDates nextDates = next != null
                    ? new BookingDates(next.getStartDate(), next.getEndDate())
                    : null;
            return ItemMapper.toItemWithDataDto(it, lastDates, nextDates, comments);
        }).toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return repository.search(text).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public Item getEntityById(Long itemId) {
        return repository.findById(itemId).orElseThrow(() -> new NotFoundException("Item with id=" + itemId + " not found"));
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, NewCommentRequest text) {
        Item currentItem = repository.findById(itemId).orElseThrow(() -> new NotFoundException("Item with id=" + itemId + " not found"));
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));;

        List<Booking> bookings = bookingRepository.findByItem_IdAndBooker_IdAndStatus(itemId, userId, Status.APPROVED);
        if (bookings.isEmpty()) {
            throw new BadRequestException("Leaving comment is forbidden: wrong user");
        }

        boolean hasFinishedBooking = bookings.stream()
                .anyMatch(b -> b.getEndDate().isBefore(LocalDateTime.now()));

        if (!hasFinishedBooking) {
            throw new BadRequestException("Leaving comment is forbidden: booking has not ended yet");
        }
        Comment newComment = commentRepository.save(CommentMapper.toComment(text, currentItem, currentUser));

        return CommentMapper.toCommentDto(newComment);
    }
}
