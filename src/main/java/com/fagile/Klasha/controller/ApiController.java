package com.fagile.Klasha.controller;

import com.fagile.Klasha.CustomException;
import com.fagile.Klasha.dto.CityDto;
import com.fagile.Klasha.dto.CountryInfoDto;
import com.fagile.Klasha.dto.StateAndCitiesDto;
import com.fagile.Klasha.service.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final CountryService apiService;

    @GetMapping("/top-cities")
    public ResponseEntity<List<CityDto>> getTopNCities(@RequestParam int numberOfCities) {
        try {
            List<CityDto> topNCities = apiService.fetchTopNCitiesFromCountries(numberOfCities);
            return new ResponseEntity<>(topNCities, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/country-info")
    public ResponseEntity<CountryInfoDto> fetchCountryInfo(@RequestParam String country) {
        if (country == null || country.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            CountryInfoDto countryInfo = apiService.fetchCountryInfo(country);
            if (countryInfo != null) {
                return new ResponseEntity<>(countryInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/states-and-cities")
    public ResponseEntity<List<StateAndCitiesDto>> getStatesAndCities(@RequestParam("country") String country) throws JsonProcessingException {
        try {
            List<StateAndCitiesDto> statesAndCities = apiService.fetchStatesAndCitiesInACountry(country);
            if (!statesAndCities.isEmpty()) {
                return new ResponseEntity<>(statesAndCities, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/convert-currency")
    public ResponseEntity<String> convertCurrency(
            @RequestParam("country") String country,
            @RequestParam("amount") double amount,
            @RequestParam("targetCurrency") String targetCurrency) {

        try {
            String convertedAmount = apiService.convertAndFormatCurrency(country, amount, targetCurrency);
            if (convertedAmount != null && !convertedAmount.isEmpty()) {
                return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Conversion unsuccessful.", HttpStatus.BAD_REQUEST);
            }
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

