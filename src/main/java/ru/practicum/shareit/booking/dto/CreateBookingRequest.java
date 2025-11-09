package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CreateBookingRequest {
    @NotNull(message = "Item id is required")
    Long itemId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    LocalDateTime start;

    @NotNull(message = "End date is required")
    @Future(message = "End date cannot be in the past")
    LocalDateTime end;

    @AssertTrue(message = "Start date must be earlier than end date")
    public boolean areDatesValid() {
        if (start == null || end == null) {
            return true;
        }
        return start.isBefore(end);
    }
}
