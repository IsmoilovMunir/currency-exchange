package ru.exchange.service;

import ru.exchange.dao.CurrencyDao;
import ru.exchange.dto.CurrencyDto;
import ru.exchange.dto.CurrencyRequestDto;
import ru.exchange.dto.CurrencyResponseDto;
import ru.exchange.model.CurrencyModel;

import java.util.List;
import java.util.stream.Collectors;

public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public List<CurrencyDto> findAll() {
        return currencyDao.findAll().stream().map(currency ->
                new CurrencyDto(currency.getId(),
                        currency.getCode(),
                        currency.getName(),
                        currency.getSign())).collect(Collectors.toList());
    }

    public CurrencyDto findByCode(String code) {
        CurrencyModel currencyModel = currencyDao.findByCode(code);
        if (currencyModel == null) {
            return null; // или выбросьте исключение, например, new IllegalArgumentException("Currency not found")
        }
        // Преобразуем модель в DTO
        return new CurrencyDto(
                currencyModel.getId(),
                currencyModel.getCode(),
                currencyModel.getName(),
                currencyModel.getSign()
        );
    }
    public CurrencyResponseDto save(CurrencyRequestDto dto){
        CurrencyModel currency  = new CurrencyModel();
        currency.setCode(dto.getCode());
        currency.setName(dto.getName());
        currency.setSign(dto.getSign());

        CurrencyModel savedCurrency = currencyDao.save(currency);
        return new CurrencyResponseDto(
                savedCurrency.getId(),
                savedCurrency.getCode(),
                savedCurrency.getName(),
                savedCurrency.getSign()
        );
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public CurrenciesService() {
    }
}
