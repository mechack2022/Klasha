package com.fagile.Klasha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryInfoDto {

        private long population;
        private String capital;
        private String location;
        private String currency;
        private String iso2;
        private String iso3;

        public boolean hasAllRequiredFields() {
                return this.population != 0 &&
                        this.capital != null &&
                        this.location != null &&
                        this.currency != null &&
                        this.iso2 != null &&
                        this.iso3 != null;
        }

    }


