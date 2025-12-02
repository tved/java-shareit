package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto createBooking(Long userId, CreateBookingRequest request) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        Long itemId = request.getItemId();
        Item currentItem = itemService.getEntityById(itemId);
        Long ownerId = currentItem.getOwner().getId();
        if (ownerId.equals(userId)) {
            throw new ForbiddenException("User with id=" + userId + " as the owner cannot book item id=" + itemId);
        }

        if (!currentItem.getAvailable()) {
            throw new BadRequestException("Item with id=" + itemId + " is not available for booking");
        }

        Booking newBooking = BookingMapper.toBooking(request, userId, Status.WAITING);
        newBooking.setItem(currentItem);
        repository.save(newBooking);
        return BookingMapper.toBookingDto(newBooking);
    }

    @Override
    public BookingDto changeBookingStatus(Long userId, Long bookingId, boolean approved) {
        Booking currentBooking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking with id=" + bookingId + " not found"));
        Long ownerId = currentBooking.getItem().getOwner().getId();

        if (!userId.equals(ownerId)) {
            throw new ForbiddenException("User with id=" + userId + " cannot approve booking id=" + bookingId);
        }

        Status newStatus = approved ? Status.APPROVED : Status.REJECTED;
        currentBooking.setStatus(newStatus);
        repository.save(currentBooking);
        return BookingMapper.toBookingDto(currentBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking currentBooking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking with id=" + bookingId + " not found"));
        Long ownerId = currentBooking.getItem().getOwner().getId();
        Long booker = currentBooking.getBooker().getId();
        if (!userId.equals(booker) && !userId.equals(ownerId)) {
            throw new ForbiddenException("User with id=" + userId + " is not the owner or a booker of item with id=" + currentBooking.getItem().getId());
        }
        return BookingMapper.toBookingDto(currentBooking);
    }

    @Override
    public List<BookingDto> getCurrentUserBookings(Long userId, BookingState state) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        List<Booking> result;
        switch (state) {
            case ALL -> result = repository.findByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "startDate"));
            case CURRENT -> result = repository.findCurrentApprovedBookings(userId, Status.APPROVED);
            case PAST -> result = repository.findPastApprovedBookings(userId, Status.APPROVED);
            case FUTURE -> result = repository.findFutureApprovedBookings(userId, Status.APPROVED);
            case WAITING -> result = repository.findBookingsByStatus(userId, Status.WAITING);
            case REJECTED -> result = repository.findBookingsByStatus(userId, Status.REJECTED);
            default -> result = List.of();
        }

        return result.stream().map(BookingMapper::toBookingDto).toList();
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long userId, BookingState state) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }

        List<Booking> result;
        switch (state) {
            case ALL -> result = repository.findByOwner(userId);
            case CURRENT -> result = repository.findCurrentBookingsByOwner(userId, Status.APPROVED);
            case PAST -> result = repository.findPastBookingsByOwner(userId, Status.APPROVED);
            case FUTURE -> result = repository.findFutureBookingsByOwner(userId, Status.APPROVED);
            case WAITING -> result = repository.findByStatusAndItem_Owner_Id(Status.WAITING, userId, Sort.by(Sort.Direction.DESC, "startDate"));
            case REJECTED -> result = repository.findByStatusAndItem_Owner_Id(Status.REJECTED, userId, Sort.by(Sort.Direction.DESC, "startDate"));
            default -> result = List.of();
        }

        return result.stream().map(BookingMapper::toBookingDto).toList();
    }
}
