package com.fagile.Klasha.service;
import com.fagile.Klasha.dto.CityDto;
import com.fagile.Klasha.dto.CountryInfoDto;
import com.fagile.Klasha.dto.StateAndCitiesDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface CountryService {
    List<CityDto> fetchTopNCitiesFromCountries(int numberOfCities) throws IOException;
    CountryInfoDto fetchCountryInfo(String country);
    List<StateAndCitiesDto> fetchStatesAndCities(String country);
    String convertAndFormatCurrency(String country, double amount, String targetCurrency);
    public List<StateAndCitiesDto> fetchStatesAndCitiesInACountry(String country) throws JsonProcessingException;

}


