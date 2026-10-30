package com.example.parking.controller;

import com.example.parking.dto.RevenueResponse;
import com.example.parking.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue")
    @Operation(summary = "Aggregate revenue for a date (yyyy-MM-dd)")
    public RevenueResponse revenue(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @PastOrPresent(message = "date cannot be in the future") LocalDate date) {
        return reportService.revenueFor(date);
    }
}
