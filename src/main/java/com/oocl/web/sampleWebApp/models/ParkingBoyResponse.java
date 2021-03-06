package com.oocl.web.sampleWebApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingLot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingBoyResponse {
    private String employeeId;
    private List<ParkingLotResponse> parkingLot;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<ParkingLotResponse> getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(List<ParkingLotResponse> parkingLot) {
        this.parkingLot = parkingLot;
    }

    public static ParkingBoyResponse create(String employeeId, List<ParkingLot> parkingLots) {
        Objects.requireNonNull(employeeId);

        final ParkingBoyResponse response = new ParkingBoyResponse();
        response.setEmployeeId(employeeId);
        if(parkingLots!=null) {
            List<ParkingLotResponse> lotResponses=new ArrayList<>();
            for(ParkingLot lot:parkingLots) {
                lotResponses.add(ParkingLotResponse.create(lot));
            }
            response.setParkingLot(lotResponses);
        }
        return response;
    }

    public static ParkingBoyResponse create(ParkingBoy entity) {
        return create(entity.getEmployeeId(),null);
    }
    public static ParkingBoyResponse create(ParkingBoy entity, List<ParkingLot> parkingLots) {
        return create(entity.getEmployeeId(),parkingLots);
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}
