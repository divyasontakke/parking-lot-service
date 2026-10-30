package com.example.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record BookingRequest(
        @NotBlank(message = "vehicleNumber is required")
        @Pattern(regexp = "^[A-Za-z]{2}[ -]?[0-9]{1,2}[ -]?[A-Za-z]{1,3}[ -]?[0-9]{4}$",
                 message = "vehicleNumber must look like MH31AB1234 (spaces/hyphens allowed)")
        String vehicleNumber,

        @NotNull(message = "slotId is required")
        @Positive(message = "slotId must be a positive number")
        Long slotId,

        @PastOrPresent(message = "entryTime cannot be in the future")
        LocalDateTime entryTime) { }
