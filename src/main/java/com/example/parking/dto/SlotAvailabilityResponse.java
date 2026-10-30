package com.example.parking.dto;

import java.util.List;

public record SlotAvailabilityResponse(
        long totalSlots,
        long occupiedSlots,
        long availableCount,
        List<SlotResponse> availableSlots) { }
