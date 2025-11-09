package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.status.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    @Query("""
       select b from Booking b
       where b.booker.id = :bookerId
         and b.status = :status
         and b.startDate < CURRENT_TIMESTAMP
         and b.endDate > CURRENT_TIMESTAMP
       order by b.startDate desc
       """)
    List<Booking> findCurrentApprovedBookings(
            @Param("bookerId") Long bookerId,
            @Param("status") Status status
    );

    @Query("""
       select b from Booking b
       where b.booker.id = :bookerId
         and b.status = :status
         and b.endDate < CURRENT_TIMESTAMP
       order by b.startDate desc
       """)
    List<Booking> findPastApprovedBookings(
            @Param("bookerId") Long bookerId,
            @Param("status") Status status
    );

    @Query("""
       select b from Booking b
       where b.booker.id = :bookerId
         and b.status = :status
         and b.startDate > CURRENT_TIMESTAMP
       order by b.startDate desc
       """)
    List<Booking> findFutureApprovedBookings(
            @Param("bookerId") Long bookerId,
            @Param("status") Status status
    );

    @Query("""
       select b from Booking b
       where b.booker.id = :bookerId
         and b.status = :status
       order by b.startDate desc
       """)
    List<Booking> findBookingsByStatus(
            @Param("bookerId") Long bookerId,
            @Param("status") Status status
    );

    @Query("""
       select b from Booking b
       where b.item.owner.id = :ownerId
       order by b.startDate desc
       """)
    List<Booking> findByOwner(@Param("ownerId") Long ownerId);

    @Query("""
       select b from Booking b
       where b.item.owner.id = :ownerId
         and b.status = :status
         and b.startDate <= CURRENT_TIMESTAMP
         and b.endDate >= CURRENT_TIMESTAMP
       order by b.startDate desc
       """)
    List<Booking> findCurrentBookingsByOwner(
            @Param("ownerId") Long ownerId,
            @Param("status") Status status
    );

    @Query("""
       select b from Booking b
       where b.item.owner.id = :ownerId
         and b.status = :status
         and b.endDate < CURRENT_TIMESTAMP
       order by b.startDate desc
       """)
    List<Booking> findPastBookingsByOwner(
            @Param("ownerId") Long ownerId,
            @Param("status") Status status
    );

    @Query("""
       select b from Booking b
       where b.item.owner.id = :ownerId
         and b.status = :status
         and b.startDate > CURRENT_TIMESTAMP
       order by b.startDate desc
       """)
    List<Booking> findFutureBookingsByOwner(
            @Param("ownerId") Long ownerId,
            @Param("status") Status status
    );

    List<Booking> findByStatusAndItem_Owner_Id(Status status, Long ownerId, Sort sort);

    List<Booking> findByItem_Id(Long itemId);

    List<Booking> findByItem_IdAndBooker_IdAndStatus(Long itemId, Long bookerId, Status status);
}
