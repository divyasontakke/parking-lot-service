package com.example.parking.controller;

import com.example.parking.dto.BookingRequest;
import com.example.parking.dto.BookingResponse;
import com.example.parking.dto.CheckoutRequest;
import com.example.parking.dto.FareResponse;
import com.example.parking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Book a slot for a vehicle on entry")
    public BookingResponse book(@Valid @RequestBody BookingRequest request) {
        return bookingService.book(request);
    }

    @GetMapping("/active")
    @Operation(summary = "All currently occupied slots / active bookings")
    public List<BookingResponse> active() {
        return bookingService.getActiveBookings();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a booking")
    public BookingResponse get(@PathVariable @Positive(message = "booking id must be positive") Long id) {
        return bookingService.getBooking(id);
    }

    @GetMapping("/{id}/fare")
    @Operation(summary = "Fare accrued so far (optionally as of a given timestamp)")
    public FareResponse fare(
            @PathVariable @Positive(message = "booking id must be positive") Long id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @PastOrPresent(message = "at cannot be in the future") LocalDateTime at) {
        return bookingService.calculateFare(id, at);
    }

    @PostMapping("/{id}/checkout")
    @Operation(summary = "Process exit, bill the vehicle and free the slot")
    public BookingResponse checkout(
            @PathVariable @Positive(message = "booking id must be positive") Long id,
            @Valid @RequestBody(required = false) CheckoutRequest request) {
        return bookingService.checkout(id, request == null ? null : request.exitTime());
    }
}
