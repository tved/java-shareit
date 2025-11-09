package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, CreateBookingRequest request);

    BookingDto changeBookingStatus(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getCurrentUserBookings(Long userId, BookingState state);

    List<BookingDto> getBookingsByOwner(Long userId, BookingState state);
}
