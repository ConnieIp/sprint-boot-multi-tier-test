package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingLotResponse[]> getAll() {
        final ParkingLotResponse[] parkingLots = parkingLotRepository.findAll().stream()
                .map(ParkingLotResponse::create)
                .toArray(ParkingLotResponse[]::new);
        return ResponseEntity.ok(parkingLots);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody ParkingLot parkingLot) {
        if(parkingLot.getParkingLotId().length()>64){
            return ResponseEntity.badRequest().build();
        }
        final ParkingLotResponse parkingLotResponse = ParkingLotResponse.create(parkingLotRepository.save(parkingLot));
        return ResponseEntity.created(URI.create("/parkinglots")).body(parkingLotResponse);
    }


}
