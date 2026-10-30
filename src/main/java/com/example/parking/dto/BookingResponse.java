package com.example.parking.dto;

import com.example.parking.model.Booking;
import com.example.parking.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        String vehicleNumber,
        Long slotId,
        String slotNumber,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        BigDecimal amount,
        PaymentStatus paymentStatus,
        String status) {

    /** Must be called inside a transaction (slot is lazily loaded). */
    public static BookingResponse from(Booking b) {
        return new BookingResponse(b.getId(), b.getVehicleNumber(), b.getSlot().getId(),
                b.getSlot().getSlotNumber(), b.getEntryTime(), b.getExitTime(), b.getAmount(),
                b.getPaymentStatus(), b.isActive() ? "ACTIVE" : "COMPLETED");
    }
}
