package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "parking_lot_id", length = 64, unique = true, nullable = false)
    private String parkingLotId;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "available_position_count")
    private int availablePositionCount;
    @Column(name = "parking_boy_id")
    private String parkingBoyId;

    public ParkingLot() {

    }

    public ParkingLot(String parkingLotID, int capacity) {
        this.parkingLotId = parkingLotID;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailablePositionCount() {
        return availablePositionCount;
    }

    public String getParkingBoyId() {
        return parkingBoyId;
    }

    public void setParkingBoyId(String parkingBoyId) {
        this.parkingBoyId = parkingBoyId;
    }
}
