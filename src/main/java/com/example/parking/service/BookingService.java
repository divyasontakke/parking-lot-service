package com.example.parking.service;

import com.example.parking.dto.BookingRequest;
import com.example.parking.dto.BookingResponse;
import com.example.parking.dto.FareResponse;
import com.example.parking.exception.ConflictException;
import com.example.parking.exception.InvalidTimeException;
import com.example.parking.exception.ResourceNotFoundException;
import com.example.parking.model.Booking;
import com.example.parking.model.ParkingSlot;
import com.example.parking.model.PaymentStatus;
import com.example.parking.repository.BookingRepository;
import com.example.parking.repository.ParkingSlotRepository;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingSlotRepository slotRepository;
    private final FeeCalculator feeCalculator;
    private final Clock clock;

    public BookingService(BookingRepository bookingRepository, ParkingSlotRepository slotRepository,
                          FeeCalculator feeCalculator, Clock clock) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.feeCalculator = feeCalculator;
        this.clock = clock;
    }

    /** Allocates a slot to a vehicle on entry. */
    @Transactional
    public BookingResponse book(BookingRequest request) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime entry = request.entryTime() != null ? request.entryTime() : now;
        if (entry.isAfter(now)) {
            throw new InvalidTimeException("Entry time cannot be in the future");
        }

        String vehicle = normalize(request.vehicleNumber());

        ParkingSlot slot = slotRepository.findByIdForUpdate(request.slotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot " + request.slotId() + " not found"));
        if (slot.isOccupied()) {
            throw new ConflictException("Slot " + slot.getSlotNumber() + " is already occupied");
        }
        if (bookingRepository.existsByVehicleNumberAndExitTimeIsNull(vehicle)) {
            throw new ConflictException("Vehicle " + vehicle + " is already parked");
        }

        slot.setOccupied(true);
        Booking saved = bookingRepository.save(new Booking(vehicle, slot, entry));
        return BookingResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getActiveBookings() {
        return bookingRepository.findActiveWithSlot().stream().map(BookingResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long id) {
        return BookingResponse.from(find(id));
    }

    /** Fare accrued so far (active) or the final fare (already checked out). */
    @Transactional(readOnly = true)
    public FareResponse calculateFare(Long id, LocalDateTime at) {
        Booking b = find(id);
        boolean finalFare = !b.isActive();
        LocalDateTime until = finalFare ? b.getExitTime() : (at != null ? at : LocalDateTime.now(clock));
        BigDecimal amount = finalFare ? b.getAmount() : feeCalculator.calculate(b.getEntryTime(), until);
        return new FareResponse(b.getId(), b.getVehicleNumber(), b.getEntryTime(), until,
                Duration.between(b.getEntryTime(), until).toMinutes(),
                feeCalculator.billableBlocks(b.getEntryTime(), until),
                amount, "INR", finalFare);
    }

    /** Processes exit, bills the vehicle and frees the slot. */
    @Transactional
    public BookingResponse checkout(Long id, LocalDateTime requestedExit) {
        Booking b = bookingRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking " + id + " not found"));
        if (!b.isActive()) {
            throw new ConflictException("Booking " + id + " has already been checked out");
        }
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime exit = requestedExit != null ? requestedExit : now;
        if (exit.isAfter(now)) {
            throw new InvalidTimeException("Exit time cannot be in the future");
        }

        BigDecimal amount = feeCalculator.calculate(b.getEntryTime(), exit); // throws if exit < entry
        b.setExitTime(exit);
        b.setAmount(amount);
        b.setPaymentStatus(PaymentStatus.PAID);   // assumes payment is collected at the exit gate
        b.getSlot().setOccupied(false);
        return BookingResponse.from(b);
    }

    private Booking find(Long id) {
        return bookingRepository.findWithSlotById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking " + id + " not found"));
    }

    private static String normalize(String raw) {
        return raw.replaceAll("[ -]", "").toUpperCase(Locale.ROOT);
    }
}
