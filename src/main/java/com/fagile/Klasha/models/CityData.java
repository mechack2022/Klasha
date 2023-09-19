package com.fagile.Klasha.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class CityData {
    private String city;
    private String country;
    private List<PopulationCount> populationCounts;

}
