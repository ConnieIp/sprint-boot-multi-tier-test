package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "parking_lot_id", length = 64, unique = true, nullable = false)
    private String parkingLotID;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "available_position_count")
    private int availablePositionCount;

    public ParkingLot() {

    }

    public ParkingLot(String parkingLotID, int capacity) {
        this.parkingLotID = parkingLotID;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getParkingLotID() {
        return parkingLotID;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailablePositionCount() {
        return availablePositionCount;
    }

}
