package com.fagile.Klasha.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CityDto {
    private String city;
    private String country;
    private List<PopulationCounts> populationCounts;
}


