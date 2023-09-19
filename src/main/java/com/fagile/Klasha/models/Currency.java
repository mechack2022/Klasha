package com.fagile.Klasha.models;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Currency {
    private String code;    // Currency code (e.g., USD, EUR)
    private String name;    // Currency name
    private BigDecimal exchangeRate; // Exchange rate to a base currency (e.g., USD)

}

