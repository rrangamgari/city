package com.mastercard.test.city.controller;

import com.mastercard.test.city.model.City;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class RoadConnectionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void goodRequest1() {
        ResponseEntity<String> response = restTemplate.exchange("/connected?origin=NEWARK&destination=BOSTON", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), "Yes");
    }

    @Test
    //Reverse Origin and destination
    public void goodRequest2() {
        ResponseEntity<String> response = restTemplate.exchange("/connected?origin=BOSTON&destination=NEWARK", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), "Yes");
    }
    @Test
    public void goodRequestWithNoConnectivity1() {
        ResponseEntity<String> response = restTemplate.exchange("/connected?origin=NEWARK&destination=TRENTON", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), "No");
    }

    @Test
    //Reverse Origin and destination
    public void goodRequestWithNoConnectivity2() {
        ResponseEntity<String> response = restTemplate.exchange("/connected?origin=BOSTON&destination=PHILADELPHIA", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), "No");
    }

    @Test
    //Reverse Origin and destination
    public void goodRequestWithNoCitiesinList() {
        ResponseEntity<String> response = restTemplate.exchange("/connected?origin=Draper&destination=Phoenix", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response.getBody(), "No");
    }

    @Test
    public void badRequest1() {
        ResponseEntity<String> response = restTemplate.exchange("/connected?origin=none", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void badRequest2() {
        ResponseEntity<String> response = restTemplate.exchange("/connected", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}