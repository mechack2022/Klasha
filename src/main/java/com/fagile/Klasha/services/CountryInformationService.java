package com.fagile.Klasha.services;

import com.fagile.Klasha.models.City;
import com.fagile.Klasha.models.CountryStatesAndCities;

import java.util.List;

public interface CountryInformationService {

    List<City> getMostPopulatedCities(int n);
     List<City> getMostPopulatedCities1();

    //     CountryInfo getCountryInfo(String countryName);
    CountryStatesAndCities getCountryStatesAndCities(String countryName);

//     ConvertedCurrency convertCurrency(String countryName, double amount, String targetCurrency);
}
