package com.fagile.Klasha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyConversionDtO {
    private String sourceCurrency;
    private String targetCurrency;
    private double originalAmount;
    private double convertedAmount;
}
