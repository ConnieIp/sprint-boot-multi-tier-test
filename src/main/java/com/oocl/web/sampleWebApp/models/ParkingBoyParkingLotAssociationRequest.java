package com.oocl.web.sampleWebApp.models;

public class ParkingBoyParkingLotAssociationRequest {

    private String parkingLotId;

    public ParkingBoyParkingLotAssociationRequest() {
    }

    public ParkingBoyParkingLotAssociationRequest(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }
}
