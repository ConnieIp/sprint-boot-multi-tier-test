package com.oocl.web.sampleWebApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
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

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingBoyResourceTests {
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
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
        final ParkingBoy boy = new ParkingBoy("PD0001");

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingboys").content(asJsonString(boy)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        final ParkingBoy createdBoy = parkingBoyRepository.findAll().get(0);
        entityManager.clear();

        assertEquals("PD0001", createdBoy.getEmployeeId());
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
