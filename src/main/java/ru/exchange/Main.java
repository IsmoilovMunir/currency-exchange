package ru.exchange;

import ru.exchange.dao.CurrencyDao;
import ru.exchange.dao.ExchangeRatesDao;
import ru.exchange.model.CurrencyModel;
import ru.exchange.model.ExchangeRates;

public class Main {
    public static void main(String[] args) {
        var exchange = ExchangeRatesDao.getInstance();
        System.out.println(exchange.findAll());
//        var currency = CurrencyDao.getInstance();
//        System.out.println(currency.findAll());
//        System.out.println("_____________");
//
//        System.out.println(currency.findByCode("AUD"));
//
//        CurrencyModel currencyDao = new CurrencyModel();
//        currencyDao.setCode("TJS");
//        currencyDao.setFullName("Tajikistan");
//        currencyDao.setSign("C.");
//
//
//      //  System.out.println(currency.save(currencyDao));
//        System.out.println("_____________");
//        System.out.println(currency.findByCode("TJS"));

        var exchangeRatesDao = ExchangeRatesDao.getInstance();
//        System.out.println(exchangeRates.findAll());
//        ExchangeRates exchangeRates = new ExchangeRates();
//        exchangeRates.setBaseCurrencyId(3L);
//        exchangeRates.setTargetCurrencyId(7L);
//        exchangeRates.setRate(29.5);
//        exchangeRates.setId(8L);
//        System.out.println(exchangeRatesDao.update(exchangeRates));

//        System.out.println(exchangeRatesDao.findByCodes("2", "1"));


    }

}
