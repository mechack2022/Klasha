package com.fagile.Klasha.serviceImp;

import com.fagile.Klasha.CustomException;
import com.fagile.Klasha.dto.*;
import com.fagile.Klasha.service.ApiCallService;
import com.fagile.Klasha.service.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final ApiCallService apiCallService;

    private final RestTemplate restTemplate;

    @Override
    public List<CityDto> fetchTopNCitiesFromCountries(int numberOfCities) throws IOException {
        List<CityDto> allCities = new ArrayList<>();
        List<String> countries = Arrays.asList("Italy","New Zealand", "Ghana");
        for (String country : countries) {
            List<CityDto> citiesFromCountry = fetchCitiesFromCountry(country, numberOfCities);
            log.info("country {}", country);
            allCities.addAll(citiesFromCountry);
            log.info("citiesFromCountry {}", citiesFromCountry);
        }
        log.info("list of Cities {}", allCities);
        return allCities;
    }


    private List<CityDto> fetchCitiesFromCountry(String country, int limit) throws IOException {
        FetchTopCitiesReqDto fetchTopCitiesReqDto = new FetchTopCitiesReqDto();
        fetchTopCitiesReqDto.setCountry(country);
        fetchTopCitiesReqDto.setLimit(limit);
        fetchTopCitiesReqDto.setOrder("desc");
        fetchTopCitiesReqDto.setOrderBy("name");
        log.info("fetchTopCitiesDto {}", fetchTopCitiesReqDto);

//        curl --location 'https://countriesnow.space/api/v0.1/countries/population/cities/filter' \
//        --data '{
//        "limit": 10,
//                "order": "asc",
//                "orderBy": "name",
//                "country": "nigeria"
//    }'

        ResponseEntity<Object> fetchTopResponse = apiCallService.getCountries("https://countriesnow.space/api/v0.1/countries/population/cities/filter/q?limit="+ limit +"&order=desc&orderBy=population&country=" + country);
        log.info("fetchTopResponse {} ", fetchTopResponse);
        if (fetchTopResponse.getStatusCode().is2xxSuccessful() && fetchTopResponse.hasBody()){
            Map<String, Object> body = (Map<String, Object> ) fetchTopResponse.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(new JSONObject(body).toJSONString());
            JsonNode dataNode = root.get("data");
            if(dataNode != null){
                return objectMapper.readValue(dataNode.toString(), new TypeReference<>() {});
            } else {
                return Collections.EMPTY_LIST;
            }
        } else {
            return Collections.EMPTY_LIST;
        }
    }


    public CountryInfoDto fetchCountryInfo(String country) {
        CountryInfoDto countryInfo = new CountryInfoDto();
        Map<String, String> params = new HashMap<>();
        params.put("country", country);

        try {
            // Fetch population
            ResponseEntity<Object> populationResponse = apiCallService.fetchSingleCountryPopulation("https://countriesnow.space/api/v0.1/countries/population", params);
            if (populationResponse.getStatusCode().is2xxSuccessful() && populationResponse.getBody() != null) {
                log.info("populationResponse: {}  :", populationResponse.getBody());
                // Extract and set the population data in the countryInfo object
                // Assuming that the response body contains population data
                countryInfo.setPopulation(Long.parseLong(((Map<String, Object>) populationResponse.getBody()).get("population").toString()));
            }else {
                log.info(" Can not get popultaion response ");
            }

            // Fetch capital city
            ResponseEntity<Object> capitalResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/capital", params);
            if (capitalResponse.getStatusCode().is2xxSuccessful() && capitalResponse.getBody() != null) {
//                countryInfo.setCapital(((Map<String, Object>) capitalResponse.getBody()).get("capital").toString());
            }

            // Fetch location
            ResponseEntity<Object> locationResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/positions", params);
            if (locationResponse.getStatusCode().is2xxSuccessful() && locationResponse.getBody() != null) {
                countryInfo.setLocation(((Map<String, Object>) locationResponse.getBody()).get("location").toString());
            }

            // Fetch currency
            ResponseEntity<Object> currencyResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/currency", params);
            if (currencyResponse.getStatusCode().is2xxSuccessful() && currencyResponse.getBody() != null) {
                countryInfo.setCurrency(((Map<String, Object>) currencyResponse.getBody()).get("currency").toString());
            }

            // Fetch ISO2 & ISO3
            ResponseEntity<Object> isoResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/iso", params);
            if (isoResponse.getStatusCode().is2xxSuccessful() && isoResponse.getBody() != null) {
                countryInfo.setIso2(((Map<String, Object>) isoResponse.getBody()).get("iso2").toString());
                countryInfo.setIso3(((Map<String, Object>) isoResponse.getBody()).get("iso3").toString());
            }

        } catch (Exception e) {
            // Handle exceptions and log errors
            e.printStackTrace();
        }

        return countryInfo;
    }


//    @Override
//    public CountryInfoDto fetchCountryInfo(String country) {
//        CountryInfoDto countryInfo = new CountryInfoDto();
//        Map<String, String> params = new HashMap<>();
//        params.put("country", country);
//
//        try {
////             Fetch population
//            ResponseEntity<Object> populationResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/population", params);
////             log.info("PopulationResponse : {}", populationResponse);
//            log.info("i GOT ABOVE THE POPULATION RESPONSE");
//              countryInfo.setPopulation(Long.parseLong(Objects.requireNonNull((Map<String, Object>)populationResponse.getBody()).get("population").toString()));
//             log.info(String.valueOf(countryInfo.getPopulation()));
//             log.info("i GOT BELOW THE POPULATION RESPONSE");
//            // Fetch capital city
//            ResponseEntity<Object> capitalResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/capital", params);
//            countryInfo.setCapital(Objects.requireNonNull((Map<String, Object>)capitalResponse.getBody()).get("capital").toString());
//
//            // Fetch location
//            ResponseEntity<Object> locationResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/positions", params);
//            countryInfo.setLocation(Objects.requireNonNull((Map<String, Object>)locationResponse.getBody()).get("location").toString());
//
//            // Fetch currency
//            ResponseEntity<Object> currencyResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/currency", params);
//            countryInfo.setCurrency(Objects.requireNonNull((Map<String, Object>)currencyResponse.getBody()).get("currency").toString());
//
//            // Fetch ISO2 & ISO3
//            ResponseEntity<Object> isoResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/iso", params);
//            countryInfo.setIso2(Objects.requireNonNull((Map<String, Object>)isoResponse.getBody()).get("iso2").toString());
//            countryInfo.setIso3(Objects.requireNonNull((Map<String, Object>)isoResponse.getBody()).get("iso3").toString());
//
//            if (countryInfo.hasAllRequiredFields()) {
//                return countryInfo;
//            } else {
//                log.info("Required fields are missing");
//            }
//
//        } catch (Exception e) {
//            // Handle exceptions and log errors
//        }
//        return new CountryInfoDto();  // Return a default object or throw an exception
//    }


    @Override
    public List<StateAndCitiesDto> fetchStatesAndCitiesInACountry(String country) {

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("country", country);

        try {
            ResponseEntity<Object> response = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/states/q?country=" + country, requestBody);
//            log.info("response: {}", response);
            List<StateAndCitiesDto> statesAndCity = new ArrayList<>();
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                ObjectMapper objectMapper = new ObjectMapper();
                List<State> states = objectMapper.readValue(getJsonNode(objectMapper, data, "states").toString(), new TypeReference<>() {});
                for (State state: states) {
                    StateAndCitiesDto stateAndCitiesDto = new StateAndCitiesDto();
                    requestBody.put("state", state.getName());
                    ResponseEntity<Object> cityResponse = apiCallService.fetchCountryInfo("https://countriesnow.space/api/v0.1/countries/state/cities/q?country=" + country +"&state=" + state.getName(), requestBody);
                    log.info("response target: {}", cityResponse);
                    stateAndCitiesDto.setStateName(state.getName());
                    List<String> data1 = objectMapper.readValue(getJsonNode(objectMapper, (Map<String, Object>) cityResponse.getBody(), "data").toString(), new TypeReference<>() {
                    });
                    stateAndCitiesDto.setCities(data1);
                    statesAndCity.add(stateAndCitiesDto);
                }
            }
            return statesAndCity;
        } catch (JsonProcessingException e) {
            log.info("error: {}", e.getMessage());
        }
        return new ArrayList<>();
    }

    private JsonNode getJsonNode(ObjectMapper objectMapper, Map<String, Object> responseMap, String data) throws JsonProcessingException {
        JsonNode cityRoot = objectMapper.readTree(new JSONObject(responseMap).toJSONString());
        return cityRoot.get(data);
    }

    @Override
    public List<StateAndCitiesDto> fetchStatesAndCities(String country) {
        String url = "https://countriesnow.space/api/v0.1/countries/state/cities";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("country", country);

        try {
            ResponseEntity<StateAndCitiesDto[]> response = restTemplate.postForEntity(url, requestBody, StateAndCitiesDto[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                StateAndCitiesDto[] statesAndCities = response.getBody();
                if (statesAndCities != null && statesAndCities.length > 0) {
                    return List.of(statesAndCities);
                } else {
                    throw new CustomException("No states and cities found for the given country.");
                }
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("API call failed with HTTP status code: {}", e.getStatusCode());
            throw new CustomException("External API call failed. Please try again later.");
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new CustomException("An unexpected error occurred. Please try again later.");
        }

        throw new CustomException("An unexpected scenario occurred.");
    }


    @Override
    public String convertAndFormatCurrency(String country, double amount, String targetCurrency) {
        // Get the country's currency from your data source or CSV
        String countryCurrency = getCountryCurrency(country);

        // Get conversion rate from CSV
        double conversionRate = getConversionRate(countryCurrency, targetCurrency);

        if (conversionRate <= 0) {
            throw new CustomException("Invalid conversion rate.");
        }

        // Convert the amount to the target currency
        double convertedAmount = amount * conversionRate;
        // Format it correctly
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedAmount = decimalFormat.format(convertedAmount);

        return String.format("%s %s is equivalent to %s %s", amount, countryCurrency, formattedAmount, targetCurrency);
    }

    private String getCountryCurrency(String country) {
        // Your logic here to get the currency for the given country
        return "USD";  // Example
    }
    private double getConversionRate(String fromCurrency, String toCurrency) {
        Map<String, Double> currencyRates = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File("/Users/decagon/Desktop/exchange_rate (1).csv"));
            scanner.nextLine();  // Skip the header line

            while (scanner.hasNextLine()) {
                String[] columns = scanner.nextLine().split(",");  // Splitting by comma for a typical CSV
                // Splitting by tab since it appears your CSV uses tabs
                if (columns.length >= 3) {
                    String currencyPair = columns[0] + "_" + columns[1];
                    double rate = Double.parseDouble(columns[2]);
                    currencyRates.put(currencyPair, rate);
                }
            }
        } catch (Exception e) {
            throw new CustomException("Could not read CSV file.");
        }

        Double rate = currencyRates.getOrDefault(fromCurrency + "_" + toCurrency, -1.0);
        return rate;
    }
}
