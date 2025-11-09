package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemWithDataDto extends ItemDto {
   private BookingDates lastBooking;
   private BookingDates nextBooking;
   private List<CommentDto> comments;

   public ItemWithDataDto(Long id, String name, String description, Boolean available, BookingDates lastBooking, BookingDates nextBooking, List<CommentDto> comments) {
      super(id, name, description, available);
      this.lastBooking = lastBooking;
      this.nextBooking = nextBooking;
      this.comments = comments;
   }
}
