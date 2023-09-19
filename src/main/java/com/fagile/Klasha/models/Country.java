package com.fagile.Klasha.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Country {
    private String name;        // Country name
    private String capital;     // Capital city
    private String location;    // Location information (e.g., continent)
    private String currency;    // Currency used in the country
    private String iso2;        // ISO 2-letter country code
    private String iso3;        // ISO 3-letter country code

}

