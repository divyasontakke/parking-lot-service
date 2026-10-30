package com.example.parking.service;

import com.example.parking.dto.RevenueResponse;
import com.example.parking.model.PaymentStatus;
import com.example.parking.repository.BookingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final BookingRepository bookingRepository;

    public ReportService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /** Revenue is attributed to the day the vehicle exited (when payment was collected). */
    public RevenueResponse revenueFor(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return new RevenueResponse(date,
                bookingRepository.sumRevenue(PaymentStatus.PAID, start, end),
                bookingRepository.countCompleted(PaymentStatus.PAID, start, end),
                "INR");
    }
}
