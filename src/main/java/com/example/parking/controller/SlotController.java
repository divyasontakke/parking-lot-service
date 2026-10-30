package com.example.parking.controller;

import com.example.parking.dto.SlotAvailabilityResponse;
import com.example.parking.dto.SlotResponse;
import com.example.parking.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/slots")
@Tag(name = "Slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/available")
    @Operation(summary = "Real-time slot availability")
    public SlotAvailabilityResponse available() {
        return slotService.getAvailability();
    }

    @GetMapping
    @Operation(summary = "List all slots with occupancy state")
    public List<SlotResponse> all() {
        return slotService.getAllSlots();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one slot")
    public SlotResponse get(@PathVariable @Positive(message = "slot id must be positive") Long id) {
        return slotService.getSlot(id);
    }
}
