package com.softer.riderassignment;


import com.softer.riderassignment.repository.TestH2Repository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.softer.riderassignment.model.RiderAssignment;
import com.softer.riderassignment.model.AssignmentStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private TestH2Repository testH2Repository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(String.valueOf(port))
                .concat("/api/assignments");
    }

    // -------------------------------------Integration Tests ----------------------

    @Test
    public void testCreateAssignment() {

        RiderAssignment assignment = new RiderAssignment(
                null,
                7001L,
                301L,
                AssignmentStatus.ASSIGNED,
                null
        );

        RiderAssignment response = restTemplate.postForObject(
                baseUrl,
                assignment,
                RiderAssignment.class
        );

        assert response != null;
        assert response.getDeliveryId().equals(7001L);
        assert response.getRiderId().equals(301L);
    }

    @Test
    public void testGetAllAssignments() {

        List<RiderAssignment> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RiderAssignment>>() {}
        ).getBody();

        assert response != null;
    }

    @Test
    public void testGetAssignmentById() {

        RiderAssignment assignment = new RiderAssignment(
                null,
                7002L,
                302L,
                AssignmentStatus.ASSIGNED,
                null
        );

        RiderAssignment created = restTemplate.postForObject(
                baseUrl,
                assignment,
                RiderAssignment.class
        );

        RiderAssignment response = restTemplate.getForObject(
                baseUrl + "/" + created.getId(),
                RiderAssignment.class
        );

        assert response != null;
        assert response.getDeliveryId().equals(7002L);
    }





}