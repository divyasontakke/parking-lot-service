package com.example.parking.dto;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public record CheckoutRequest(
        @PastOrPresent(message = "exitTime cannot be in the future")
        LocalDateTime exitTime) { }
