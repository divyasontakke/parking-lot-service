package com.example.parking.dto;

import com.example.parking.model.ParkingSlot;

public record SlotResponse(Long id, String slotNumber, boolean occupied) {
    public static SlotResponse from(ParkingSlot s) {
        return new SlotResponse(s.getId(), s.getSlotNumber(), s.isOccupied());
    }
}
