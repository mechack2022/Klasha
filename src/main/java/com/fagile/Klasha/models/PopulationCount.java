package com.fagile.Klasha.models;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class PopulationCount {
    private String year;         // Year of the population count
    private String value;        // Population value for the year
    private String sex;          // Sex (e.g., "Both Sexes")
    private String reliability;  // Reliability level (e.g., "Final figure, complete")



}


