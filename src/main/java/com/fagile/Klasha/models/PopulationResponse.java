package com.fagile.Klasha.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class PopulationResponse {
    private boolean error;
    private String msg;
    private List<CityData> data;


}

