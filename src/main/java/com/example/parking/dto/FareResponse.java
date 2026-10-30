package com.example.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FareResponse(
        Long bookingId,
        String vehicleNumber,
        LocalDateTime entryTime,
        LocalDateTime calculatedUntil,
        long durationMinutes,
        long billableBlocks,
        BigDecimal amount,
        String currency,
        boolean finalFare) { }
