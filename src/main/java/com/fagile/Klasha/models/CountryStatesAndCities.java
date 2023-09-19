package com.fagile.Klasha.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class CountryStatesAndCities {

    private String country;
    private List<StateCities> statesAndCities;
}
