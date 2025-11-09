package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
            booking.getId(),
            booking.getStartDate(),
            booking.getEndDate(),
            ItemMapper.toItemDto(booking.getItem()),
            booking.getStatus(),
            UserMapper.toUserDto(booking.getBooker())
        );
    }

    public static Booking toBooking(CreateBookingRequest request, Long userId, Status status) {
        User booker = User.builder().id(userId).build();
        Item item = Item.builder().id(request.getItemId()).build();

        return Booking.builder()
                .startDate(request.getStart())
                .endDate(request.getEnd())
                .item(item)
                .booker(booker)
                .status(status)
                .build();
    }
}
