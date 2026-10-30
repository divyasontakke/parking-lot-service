package com.example.parking.repository;

import com.example.parking.model.ParkingSlot;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByOccupiedFalseOrderBySlotNumber();

    /** Row-level lock (SELECT ... FOR UPDATE) to serialise concurrent bookings of the same slot. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ParkingSlot s where s.id = :id")
    Optional<ParkingSlot> findByIdForUpdate(@Param("id") Long id);
}
