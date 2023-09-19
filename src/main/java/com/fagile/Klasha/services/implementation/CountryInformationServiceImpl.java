package com.fagile.Klasha.services.implementation;

import com.fagile.Klasha.models.*;
import com.fagile.Klasha.services.CountryInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryInformationServiceImpl implements CountryInformationService {

    @Value("${api.base_url}")
    private String apiBaseUrl;  //  https://countriesnow.space/api/v0.1/

    @Value("${api_cities_in_state}")
    private String apiCitiesInStateUrl;

    @Value("${api_states_in_country}")
    private  String apiStatesInCountryUrl;

    private final RestTemplate restTemplate;


    // Base URL for the provided APIs
    private static final String BASE_API_URL = "https://countriesnow.space/api/v0.1/";


    @Override
    public List<City> getMostPopulatedCities1() {
        // Construct the URL with the appropriate query parameters
        String apiUrl = "https://countriesnow.space/api/v0.1/countries/population/cities";

        // Make a GET request to the API
        ResponseEntity<PopulationResponse> responseEntity = restTemplate.getForEntity(apiUrl, PopulationResponse.class);

        // Check if the response is successful (2xx status code)
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            PopulationResponse populationResponse = responseEntity.getBody();

            // Check if the response data is not null
            if (populationResponse != null && populationResponse.getData() != null) {
                // Extract and return the list of cities
                List<CityData> cityDataList = populationResponse.getData();
                List<City> cities = convertCityDataListToCityList(cityDataList);
                return cities;
            }
        }
        // Handle the case where the API request fails or the response is empty
        return null;
    }

    @Override
    public List<City> getMostPopulatedCities(int n) {
        List<City> mostPopulatedCities = new ArrayList<>();

        // Define the list of countries to query
        List<String> countries = List.of("Italy", "New Zealand", "Ghana");

        for (String country : countries) {
            // Make a POST request to filter cities by country
            ResponseEntity<PopulationResponse> responseEntity = restTemplate.postForEntity(
                    apiBaseUrl + "countries/population/cities/filter",
                    createFilterRequestBody(n, "desc", "value", country),
                    PopulationResponse.class
            );
            log.info("I am inside get most");
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                PopulationResponse populationResponse = responseEntity.getBody();
                log.info("Population Response : {}", populationResponse);
                if (populationResponse != null && populationResponse.getData() != null) {
                    List<CityData> cityDataList = populationResponse.getData();
                    List<City> cities = convertCityDataListToCityList(cityDataList);

                    // Add the cities to the result list (up to the remaining required count)
                    int remainingCount = n - mostPopulatedCities.size();
                    mostPopulatedCities.addAll(cities.subList(0, Math.min(remainingCount, cities.size())));

                    // Check if we have gathered enough cities
                    if (mostPopulatedCities.size() >= n) {
                        break;
                    }
                }
            }
        }
        return mostPopulatedCities;
    }

    @Override
    public CountryStatesAndCities getCountryStatesAndCities(String countryName) {
        CountryStatesAndCities countryStatesAndCities = new CountryStatesAndCities();
        countryStatesAndCities.setCountry(countryName);

        //get states in the country provided
        List<String> states = getStatesInCountry(countryName);

//        For each state, get the list of cities
        List<StateCities> statesAndCities = new ArrayList<>();
        for (String state : states) {
            StateCities stateCities = new StateCities();
            stateCities.setState(state);

            List<String> cities = getCitiesInState(countryName, state);
            stateCities.setCities(cities);

            statesAndCities.add(stateCities);
        }
        countryStatesAndCities.setStatesAndCities(statesAndCities);

        return countryStatesAndCities;
    }

    // Helper method to create the filter request body
    private String createFilterRequestBody(int limit, String order, String orderBy, String country) {
        return "{" +
                "\"limit\":" + limit + "," +
                "\"order\":\"" + order + "\"," +
                "\"orderBy\":\"" + orderBy + "\"," +
                "\"country\":\"" + country + "\"" +
                "}";
    }

    // Helper method to convert CityData list to City list
    private List<City> convertCityDataListToCityList(List<CityData> cityDataList) {
        List<City> cities = new ArrayList<>();

        for (CityData cityData : cityDataList) {
            City city = new City();
            city.setName(cityData.getCity());

            // Extract population from populationCounts if available
            // Here, we are assuming the latest population count is the last element in the list
            List<PopulationCount> populationCounts = cityData.getPopulationCounts();
            if (populationCounts != null && !populationCounts.isEmpty()) {
                PopulationCount latestPopulation = populationCounts.get(populationCounts.size() - 1);
                try {
                    int population = Integer.parseInt(latestPopulation.getValue());
                    city.setPopulation(population);
                } catch (NumberFormatException e) {
                    // Handle invalid population data or errors
                    // You may log an error or set a default value here
                }
            }
            city.setCountry(cityData.getCountry());

            // Add the created City object to the list of cities
            cities.add(city);
        }

        return cities;
    }

    // get states in a country

//    curl --location 'https://countriesnow.space/api/v0.1/countries/states' \
//            --data '{
//            "country": "Nigeria"
//}'
    private List<String> getStatesInCountry(String countryName) {
        log.info("I got inside states in country");
//        String url = BASE_API_URL + "countries/states";
        String url = apiStatesInCountryUrl;
        CountryApiResponse response = restTemplate.postForObject(url, "{\"country\":\"" + countryName + "\"}", CountryApiResponse.class);

        if (response != null && !response.isError()) {
            log.info("States in country : {}", response.getData());
            return response.getData();
        }
        return new ArrayList<>();

    }

    // get the cities in a state
    private List<String> getCitiesInState(String countryName, String stateName) {
        log.info("I got inside cities in states");
//        String url = BASE_API_URL + "countries/state/cities";
        String url = apiCitiesInStateUrl;
        String requestData = "{\"country\":\"" + countryName + "\",\"state\":\"" + stateName + "\"}";
        CityApiResponse response = restTemplate.postForObject(url, requestData, CityApiResponse.class);

        if (response != null && !response.isError()) {
            log.info("Cities in State :  {} ", response.getData());
            return response.getData();
        }

        return new ArrayList<>();
    }


}






