package com.fagile.Klasha.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class ApiCallService {
    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Object> getCountries(String url) {
        ResponseEntity<Object> responseEntity;
        HttpEntity<Object> httpEntity = new HttpEntity<>(getHeaders());
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
            return responseEntity;
        } catch (RestClientException e) {
            log.info("error :{}", e.getMessage());
        }

        return null;
    }

    public ResponseEntity<Object> fetchCountryInfo(String url, Map<String, String> request) {
        ResponseEntity<Object> responseEntity;
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(getHeaders());
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
        return responseEntity;
    }

    public ResponseEntity<Object> fetchSingleCountryPopulation(String url, Map<String, String> request) {
        ResponseEntity<Object> responseEntity;
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(request, getHeaders());
        responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
        return responseEntity;
    }


    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return  headers;
    }

}
