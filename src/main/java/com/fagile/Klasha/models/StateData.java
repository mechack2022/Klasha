package com.fagile.Klasha.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class StateData {
    private String name;
    private String iso3;
    private List<StateInfo> states;

    // Constructors, getters, and setters
}
