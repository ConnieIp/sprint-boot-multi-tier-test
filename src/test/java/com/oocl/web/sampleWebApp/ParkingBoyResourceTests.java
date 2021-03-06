package com.oocl.web.sampleWebApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyParkingLotAssociationRequest;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;

import java.util.List;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingBoyResourceTests {
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    @Test
    public void should_get_parking_boys() throws Exception {
        //When GET /parkingBoys, Return 200 with a parkingBoys list [{"employeeID": "string"}]

        // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        parkingBoyRepository.flush();

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkingboys"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse[] parkingBoys = getContentAsObject(result, ParkingBoyResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals("boy", parkingBoys[0].getEmployeeId());
    }

    @Test
    public void should_create_parking_boys() throws Exception {
        //Given a parking boy {"employeeId":"boy"}, When POST /parkingBoys, Return 201
        // Given
        final ParkingBoy boy = new ParkingBoy("PB0001");

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys").content(asJsonString(boy)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        final ParkingBoy createdBoy = parkingBoyRepository.findAll().get(0);
        entityManager.clear();

        assertEquals("PB0001", createdBoy.getEmployeeId());
    }

    @Test
    public void should_not_create_parking_boys_if_parkingBoyId_length_exceed() throws Exception {
        //Given a parking boy {"employeeId":"string"} woth employeeId length>64, When POST /parkingBoys, Return 400
        // Given
        final ParkingBoy boy = new ParkingBoy("12345678901234567890123456789012345678901234567890123456789012345");

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys").content(asJsonString(boy)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        assertEquals(400, result.getResponse().getStatus());

        final List<ParkingBoy> createdBoy = parkingBoyRepository.findAll();
        entityManager.clear();

        assertEquals(0, createdBoy.size());
    }

    @Test
    public void should_not_create_parking_boys_if_parkingBoyId_already_exist() throws Exception {
        //Given a parking boy {"employeeId":"string"} with employeeId already exist, When POST /parkingBoys, Return 400
        // Given
        parkingBoyRepository.save(new ParkingBoy("PB0001"));
        parkingBoyRepository.flush();
        final ParkingBoy boy = new ParkingBoy("PB0001");

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys").content(asJsonString(boy)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        assertEquals(400, result.getResponse().getStatus());

        final List<ParkingBoy> createdBoy = parkingBoyRepository.findAll();
        entityManager.clear();

        assertEquals(1, createdBoy.size());
    }

    @Test
    public void should_add_parking_lot_to_parking_boy() throws Exception {
        //Given a parking boy parking lot association {"parkingLotId": "String"} and parking boy id, When POST /parkingBoys/{employeeID}/parkingLots, Return 201
        //Given
        final String parkingBoyId="PB0001";
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy(parkingBoyId));
        parkingBoyRepository.flush();
        final ParkingBoyParkingLotAssociationRequest associationRequest=new ParkingBoyParkingLotAssociationRequest("PL0001");
        final ParkingLot lot = parkingLotRepository.save(new ParkingLot(associationRequest.getParkingLotId(),10));
        parkingLotRepository.flush();

        //When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys/"+parkingBoyId+"/parkinglots").content(asJsonString(associationRequest)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //Then
        assertEquals(201, result.getResponse().getStatus());

        final ParkingLot boyAddedParkingLot = parkingLotRepository.findAll().get(0);
        entityManager.clear();

        assertEquals("PL0001",boyAddedParkingLot.getParkingLotId());
        assertEquals("PB0001", boyAddedParkingLot.getParkingBoyId());

    }

    @Test
    public void should_get_parking_boys_with_parking_lots() throws Exception {
        //When GET /parkingBoys/{employeeID}, Return 200 with parkingBoy {"employeeID": "string", "parkingLots": [{"parkingLotID": "string", "capacity": integer}] }

        // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        parkingBoyRepository.flush();
        final ParkingLot lot = parkingLotRepository.save(new ParkingLot("PL0001",10,"boy"));
        parkingLotRepository.flush();


        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkingboys/boy"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse parkingBoy = getContentAsObject(result, ParkingBoyResponse.class);

        assertEquals("boy", parkingBoy.getEmployeeId());
        assertEquals(1,parkingBoy.getParkingLot().size());
        assertEquals("PL0001",parkingBoy.getParkingLot().get(0).getParkingLotId());
    }

    @Test
    public void should_not_get_parking_boy_if_employeeId_not_exist() throws Exception {
        //When GET /parkingBoys/{employeeId} with wrong employeeId , Return 404

        // Given

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkingboys/boy"))
                .andReturn();

        // Then
        assertEquals(404, result.getResponse().getStatus());

    }
    @Test
    public void should_not_add_parking_lot_to_parking_boy_if_employeeId_not_exist() throws Exception {
        //When POST /parkingBoys/{employeeId}/parkinglots with wrong employeeId , Return 400

        // Given
        final ParkingBoyParkingLotAssociationRequest associationRequest=new ParkingBoyParkingLotAssociationRequest("PL0001");


        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys/boy/parkinglots").content(asJsonString(associationRequest)).contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Then
        assertEquals(400, result.getResponse().getStatus());

    }

    @Test
    public void should_not_add_parking_lot_to_parking_boy_if_parking_lot_id_not_exist() throws Exception {
        //When POST /parkingBoys/{employeeId}/parkinglots with wrong parkinglotId , Return 400

        // Given
        final ParkingBoyParkingLotAssociationRequest associationRequest=new ParkingBoyParkingLotAssociationRequest("PL0001");


        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys/boy/parkinglots").content(asJsonString(associationRequest)).contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Then
        assertEquals(400, result.getResponse().getStatus());

    }

    @Test
    public void should_not_add_parking_lot_to_parking_boy_if_parking_lot_already_have_parking_boy() throws Exception {
        //When POST /parkingBoys/{employeeId}/parkinglots with parkinglot already have parking boy , Return 409

        // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        parkingBoyRepository.flush();
        final ParkingLot lot = parkingLotRepository.save(new ParkingLot("PL0001",10,"boy"));
        parkingLotRepository.flush();
        final ParkingBoyParkingLotAssociationRequest associationRequest=new ParkingBoyParkingLotAssociationRequest("PL0001");


        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys/boy/parkinglots").content(asJsonString(associationRequest)).contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Then
        assertEquals(409, result.getResponse().getStatus());

    }



    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
