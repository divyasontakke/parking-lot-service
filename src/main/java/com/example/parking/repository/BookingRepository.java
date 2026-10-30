package com.example.parking.repository;

import com.example.parking.model.Booking;
import com.example.parking.model.PaymentStatus;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByVehicleNumberAndExitTimeIsNull(String vehicleNumber);

    @Query("select b from Booking b join fetch b.slot where b.id = :id")
    Optional<Booking> findWithSlotById(@Param("id") Long id);

    @Query("select b from Booking b join fetch b.slot where b.exitTime is null order by b.entryTime")
    List<Booking> findActiveWithSlot();

    /** Locks the booking row so two simultaneous checkouts cannot both succeed. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Booking b where b.id = :id")
    Optional<Booking> findByIdForUpdate(@Param("id") Long id);

    @Query("select coalesce(sum(b.amount), 0) from Booking b "
         + "where b.paymentStatus = :status and b.exitTime >= :start and b.exitTime < :end")
    BigDecimal sumRevenue(@Param("status") PaymentStatus status,
                          @Param("start") LocalDateTime start,
                          @Param("end") LocalDateTime end);

    @Query("select count(b) from Booking b "
         + "where b.paymentStatus = :status and b.exitTime >= :start and b.exitTime < :end")
    long countCompleted(@Param("status") PaymentStatus status,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);
}
