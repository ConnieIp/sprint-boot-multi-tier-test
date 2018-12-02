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
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity add(@RequestBody ParkingBoy parkingBoy) {
        if(parkingBoy.getEmployeeId().length()>64){
            return ResponseEntity.badRequest().build();
        }
        if(parkingBoyRepository.findOneByEmployeeId(parkingBoy.getEmployeeId())!=null){
            return ResponseEntity.badRequest().build();
        }
        final ParkingBoyResponse parkingBoyResponse = ParkingBoyResponse.create(parkingBoyRepository.save(parkingBoy));
        return ResponseEntity.created(URI.create("/parkingboys")).body(parkingBoyResponse);
    }

    @PostMapping(path = "/{employeeId}/parkinglots")
    public ResponseEntity addParkingLotToParkingBoy(@PathVariable String employeeId,@RequestBody ParkingBoyParkingLotAssociationRequest parkingBoyParkingLotAssociationRequest){
        ParkingBoy parkingBoy=parkingBoyRepository.findOneByEmployeeId(employeeId);
        if(parkingBoy==null){
            return ResponseEntity.badRequest().build();
        }
        ParkingLot parkingLot=parkingLotRepository.findOneByParkingLotId(parkingBoyParkingLotAssociationRequest.getParkingLotId());
        if(parkingLot==null){
            return ResponseEntity.badRequest().build();
        }
        if(parkingLot.getParkingBoyId()!=null){
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        parkingLot.setParkingBoyId(employeeId);
        parkingLotRepository.save(parkingLot);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<ParkingBoyResponse> getParkingBoy(@PathVariable String employeeId){
        ParkingBoy parkingBoy=parkingBoyRepository.findOneByEmployeeId(employeeId);
        if(parkingBoy==null){
            return ResponseEntity.notFound().build();
        }
        List<ParkingLot> parkingLots=parkingLotRepository.findAll().stream().filter(lot -> lot.getParkingBoyId().equals(employeeId)).collect(Collectors.toList());
        ParkingBoyResponse parkingBoyResponse=ParkingBoyResponse.create(parkingBoy,parkingLots);
        return new ResponseEntity<ParkingBoyResponse>(parkingBoyResponse,HttpStatus.OK);
    }
}
