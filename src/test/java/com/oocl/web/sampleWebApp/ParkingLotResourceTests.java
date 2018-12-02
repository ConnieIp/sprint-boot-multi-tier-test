package com.oocl.web.sampleWebApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
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

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingLotResourceTests {
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    @Test
    public void should_get_parking_lots() throws Exception {
        //When GET /parkingLots, Return 200 with parking lots list [{"parkingLotID": "string", "availablePositionCount": integer, "capacity": integer}]

        // Given
        final ParkingLot lot = parkingLotRepository.save(new ParkingLot("lot",10));
        parkingLotRepository.flush();

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkinglots"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingLotResponse[] parkingLots = getContentAsObject(result, ParkingLotResponse[].class);

        assertEquals(1, parkingLots.length);
        assertEquals("lot", parkingLots[0].getParkingLotId());
    }

    @Test
    public void should_create_parking_lots() throws Exception {
        //Given a parkinglLot {"parkingLotID": "string", "capacity": integer}, When POST /parkingLots, Return 201
        // Given
        final ParkingLot lot = new ParkingLot("PL0001",10);

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkinglots").content(asJsonString(lot)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        final ParkingLot createdLot = parkingLotRepository.findAll().get(0);
        entityManager.clear();

        assertEquals("PL0001", createdLot.getParkingLotID());
        assertEquals(10,createdLot.getCapacity());
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
