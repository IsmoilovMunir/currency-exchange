package ru.exchange;

import ru.exchange.dao.CurrencyDao;
import ru.exchange.model.CurrencyModel;

public class Main {
    public static void main(String[] args) {
        var currency = CurrencyDao.getInstance();
        System.out.println(currency.findAll());
        System.out.println("_____________");

        System.out.println(currency.findByCode("AUD"));

        CurrencyModel currencyDao = new CurrencyModel();
        currencyDao.setCode("TJS");
        currencyDao.setFullName("Tajikistan");
        currencyDao.setSign("C.");


      //  System.out.println(currency.save(currencyDao));
        System.out.println("_____________");
        System.out.println(currency.findByCode("TJS"));


    }

}
