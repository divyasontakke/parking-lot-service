package com.example.parking.service;

import com.example.parking.dto.SlotAvailabilityResponse;
import com.example.parking.dto.SlotResponse;
import com.example.parking.exception.ResourceNotFoundException;
import com.example.parking.repository.ParkingSlotRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SlotService {

    private final ParkingSlotRepository slotRepository;

    public SlotService(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public SlotAvailabilityResponse getAvailability() {
        List<SlotResponse> available = slotRepository.findByOccupiedFalseOrderBySlotNumber()
                .stream().map(SlotResponse::from).toList();
        long total = slotRepository.count();
        return new SlotAvailabilityResponse(total, total - available.size(), available.size(), available);
    }

    public List<SlotResponse> getAllSlots() {
        return slotRepository.findAll().stream().map(SlotResponse::from).toList();
    }

    public SlotResponse getSlot(Long id) {
        return slotRepository.findById(id).map(SlotResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Slot " + id + " not found"));
    }
}
