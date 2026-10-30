package com.example.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueResponse(
        LocalDate date,
        BigDecimal totalRevenue,
        long completedBookings,
        String currency) { }
