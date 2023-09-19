package com.fagile.Klasha.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class CountryApiResponse {
    private boolean error;
    private String msg;
    private List<String> data;
}
