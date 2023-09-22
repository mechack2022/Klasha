package com.fagile.Klasha.dto;

import lombok.Data;

@Data
public class FetchTopCitiesReqDto {
    private int limit;
    private String order;
    private String orderBy;
    private String country;
}
