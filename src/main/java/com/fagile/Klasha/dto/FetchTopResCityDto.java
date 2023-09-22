package com.fagile.Klasha.dto;

import lombok.Data;

import java.util.List;

@Data
public class FetchTopResCityDto {
    private boolean error;
    private String message;
    List<CityDto> data;

    public static FetchTopResCityDto success(final List<CityDto> data) {
        return new FetchTopResCityDto("successful", data);
    }
    public static FetchTopResCityDto failure(final String status, final boolean error) {
        return new FetchTopResCityDto(status, error);
    }

    private FetchTopResCityDto(final String message, final boolean error) {
        this.message = message;
        this.error = error;
    }

    private FetchTopResCityDto(final String message, final List<CityDto> data) {
        this.message = message;
        this.data = data;
        this.error = false;
    }
}
