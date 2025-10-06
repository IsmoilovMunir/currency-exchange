package ru.exchange;

import ru.exchange.dao.CurrencyDao;
import ru.exchange.dao.ExchangeRatesDao;
import ru.exchange.model.CurrencyModel;
import ru.exchange.model.ExchangeRates;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        var dao = ExchangeRatesDao.getInstance();
        var currenciesDao =CurrencyDao.getInstance();
        System.out.println(currenciesDao.findAll());

        // Поиск курса

        //System.out.println(dao.findAll());
        //ExchangeRates newRate = new ExchangeRates(null, usd, eur, new BigDecimal("0.98"));

       // System.out.println(dao.save(newRate));

    }

}
