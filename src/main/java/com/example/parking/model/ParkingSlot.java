package com.example.parking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slot_number", nullable = false, unique = true, length = 20)
    private String slotNumber;

    @Column(nullable = false)
    private boolean occupied;

    protected ParkingSlot() { }

    public ParkingSlot(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Long getId() { return id; }
    public String getSlotNumber() { return slotNumber; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}
