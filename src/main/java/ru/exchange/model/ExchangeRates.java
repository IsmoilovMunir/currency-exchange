package ru.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRates {
    private Long id;
    private Long baseCurrencyId;
    private Long targetCurrencyId;
    private Double rate;
}
