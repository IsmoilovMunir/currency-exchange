package ru.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExchangeRatesDto {
    private Long id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private Double rate;
}
