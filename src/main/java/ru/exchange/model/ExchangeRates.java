package ru.exchange.model;

import lombok.Data;

@Data
public class ExchangeRates {
    private Long id;
    private Long baseCurrencyId;
    private Long targetCurrencyId;
    private Double rate;
}
