package com.fagile.Klasha.controller;

import com.fagile.Klasha.models.*;
import com.fagile.Klasha.services.CountryInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/countryInfo")
@Slf4j
public class CityController {

    private final RestTemplate restTemplate;
    @Value("${api_states_in_country}")
    private String apiStatesInCountryUrl;
    private final CountryInformationService countryInformationService;

    @Autowired
    public CityController(CountryInformationService cityPopulationService, RestTemplate restTemplate) {
        this.countryInformationService = cityPopulationService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/most-populated-cities1")
    public List<City> getMostPopulatedCities1(@RequestParam(value = "numberOfCities") int numberOfCities) {
        // Call the service to retrieve the most populated cities
        List<City> mostPopulatedCities = countryInformationService.getMostPopulatedCities1();

        // Handle the case where the response is null or empty
        if (mostPopulatedCities == null || mostPopulatedCities.isEmpty()) {
            // You can return an appropriate response or throw an exception
            // Here, we return an empty list, but you can customize this logic
            return mostPopulatedCities;
        }
        // Return the list of most populated cities
        return mostPopulatedCities;
    }


    @GetMapping("/most-populated-cities")
    public List<City> getMostPopulatedCities(@RequestParam(value = "numberOfCities") int numberOfCities) {
        // Call the service to retrieve the most populated cities

        // Return the list of most populated cities directly
        return countryInformationService.getMostPopulatedCities(numberOfCities);
    }

    @GetMapping("/country-states-and-cities")
    public CountryStatesAndCities getCountryStatesAndCities(@RequestParam(value = "country") String country) {
        return countryInformationService.getCountryStatesAndCities(country);
    }

    @GetMapping("/get-states-in-country")
    public List<StateInfo> getAllstatesInCountry(@RequestParam(value="country") String country) {
        // Define the API URL
        String apiUrl = "https://countriesnow.space/api/v0.1/countries/states";

        // Create the request body
        String requestBody = "{\"country\": \"" + country + "\"}";
        log.info("I am before the calling of the external api");
        // Make the API call and retrieve the response
        StateApiResponse response = restTemplate.postForObject(apiUrl, requestBody, StateApiResponse.class);
        log.info("I am after  calling  the external api");
        if (response != null && !response.isError() && response.getData() != null) {
            log.info("I am before the response to return");
            return response.getData().getStates();
        } else {

            // Handle error or return an empty list
            return Collections.emptyList();
        }
    }


}