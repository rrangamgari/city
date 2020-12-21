package com.mastercard.test.city.controller;

import com.mastercard.test.city.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@RestController
public class RoadConnectionController {

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(
            value = "/echo/{echo}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> connected(
            @PathVariable("echo") String echo) {
        return new ResponseEntity<>(echo, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/connected",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> connected(
            @RequestParam String origin, @RequestParam String destination) {
        String response = "No";

        try {
            if (cityList != null && cityList.size() > 0) {
                List list1 = cityList.stream().filter(city ->
                        (city.getOrigin().equalsIgnoreCase(origin) && city.getDestination().equalsIgnoreCase(destination)) ||
                                (city.getOrigin().equalsIgnoreCase(destination) && city.getDestination().equalsIgnoreCase(origin))).collect(Collectors.toList());
                if (list1.size() > 0) {
                    response = "Yes";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Value("${data.file:classpath:city.txt}")
    private String citiesFile;

    private List<City> cityList = new ArrayList<>();

    @PostConstruct
    private void read() throws IOException {


        Resource resource = resourceLoader.getResource(citiesFile);

        InputStream is = null;

        if (!resource.exists()) {
            // file on the filesystem path
            is = new FileInputStream(new File(citiesFile));
        } else {
            // file is a classpath resource
            is = resource.getInputStream();
        }

        Scanner scanner = new Scanner(is);

        while (scanner.hasNext()) {

            String line = scanner.nextLine();
            if (StringUtils.isEmpty(line)) continue;


            String[] split = line.split(",");
            String from = split[0].trim().toUpperCase();
            String to = split[1].trim().toUpperCase();
            System.out.println("from : " + from);
            System.out.println("to : " + to);

            City city = new City();
            city.setOrigin(from);
            city.setDestination(to);
            cityList.add(city);

        }

    }

}
