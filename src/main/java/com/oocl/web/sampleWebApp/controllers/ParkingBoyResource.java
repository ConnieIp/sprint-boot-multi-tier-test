package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyParkingLotAssociationRequest;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAll() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
            .map(ParkingBoyResponse::create)
            .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }

    @PostMapping
    public ResponseEntity<ParkingBoyResponse> add(@RequestBody ParkingBoy parkingBoy) {
        final ParkingBoyResponse parkingBoyResponse = ParkingBoyResponse.create(parkingBoyRepository.save(parkingBoy));
        return ResponseEntity.created(URI.create("/parkingboys")).body(parkingBoyResponse);
    }

    @PostMapping(path = "/{employeeId}/parkinglots")
    public ResponseEntity addParkingLotToParkingBoy(@PathVariable String employeeId,@RequestBody ParkingBoyParkingLotAssociationRequest parkingBoyParkingLotAssociationRequest){
        ParkingBoy parkingBoy=parkingBoyRepository.findOneByEmployeeId(employeeId);
        ParkingLot parkingLot=parkingLotRepository.findOneByParkingLotId(parkingBoyParkingLotAssociationRequest.getParkingLotId());
        parkingLot.setParkingBoyId(employeeId);
        parkingLotRepository.save(parkingLot);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
