package ru.exchange.service;

import ru.exchange.dao.CurrencyDao;
import ru.exchange.dao.ExchangeRatesDao;
import ru.exchange.dto.ExchangeRatesDto;
import ru.exchange.dto.ExchangeRequestDto;
import ru.exchange.dto.ExchangeResponseDto;
import ru.exchange.model.CurrencyModel;
import ru.exchange.model.ExchangeRates;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    public List<ExchangeRatesDto> findAll() {
        return exchangeRatesDao.findAll().stream().map(
                exchangeRates -> new ExchangeRatesDto(
                        exchangeRates.getId(),
                        exchangeRates.getBaseCurrency(),
                        exchangeRates.getTargetCurrency(),
                        exchangeRates.getRate())
        ).collect(Collectors.toList()).reversed();
    }

    public ExchangeResponseDto save(String baseCode, String targetCode, BigDecimal rate) {
        CurrencyModel baseCurrency = currencyDao.findByCode(baseCode);
        CurrencyModel targetCurrency = currencyDao.findByCode(targetCode);
        if (baseCurrency == null || targetCurrency == null) {
            return null; // 404
        }
// Проверяем, существует ли уже пара
        ExchangeRates existing = exchangeRatesDao.findByCodes(baseCode, targetCode);
        if (existing != null) {
            throw new IllegalStateException("Exchange rate already exists");
        }

        // Создаём новый курс
        ExchangeRates newRate = new ExchangeRates();
        newRate.setBaseCurrency(baseCurrency);
        newRate.setTargetCurrency(targetCurrency);
        newRate.setRate(rate);

        ExchangeRates saved = exchangeRatesDao.save(newRate);

        return new ExchangeResponseDto(
                saved.getId(),
                saved.getBaseCurrency(),
                saved.getTargetCurrency(),
                saved.getRate()
        );
    }

    public ExchangeRatesDto findByCode(String baseCode, String targetCode) {
        ExchangeRates exchangeRates = exchangeRatesDao.findByCodes(baseCode, targetCode);
        if (exchangeRates == null) {
            return null; // или выбросьте исключение, например, new IllegalArgumentException("Currency not found")
        }
        return new ExchangeRatesDto(
                exchangeRates.getId(),
                exchangeRates.getBaseCurrency(),
                exchangeRates.getTargetCurrency(),
                exchangeRates.getRate());
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesService() {
    }


}
