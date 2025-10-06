package ru.exchange.dao;

import ru.exchange.db.utils.ConnectionManger;
import ru.exchange.model.CurrencyModel;
import ru.exchange.model.ExchangeRates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ExchangeRatesDao {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private static final String FIND_ALL_SQL = """
            SELECT
             e.id AS rate_id,
             e.rate,
             b.id AS base_id,
             b.name AS base_name,
             b.code      AS base_code,
             b.sign      AS base_sign,
             t.id AS target_id,
             t.name AS target_name,
              t.code      AS target_code,
              t.sign      AS target_sign
               FROM exchange_rates e
            JOIN currencies b ON e.base_currency_id = b.id
            JOIN currencies t ON e.target_currency_id = t.id
            
            """;
    private static final String FIND_BY_CODES = FIND_ALL_SQL + """
            WHERE b.code = ? AND t.code = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates
            (base_currency_id, target_currency_id, rate)
            VALUES (?,?,?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE exchange_rates
            SET base_currency_id = ?,
            target_currency_id = ?,
            rate = ?
            WHERE id = ?
            """;

    public ExchangeRates update(ExchangeRates exchangeRates) {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setLong(1, exchangeRates.getBaseCurrency().getId());
            statement.setLong(2, exchangeRates.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRates.getRate());
            statement.setLong(4, exchangeRates.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {// No record was found with that ID, so you should return a 404
                // This might require throwing a custom exception handled in your web layer
                throw new RuntimeException("Exchange rate with id " + exchangeRates.getId() + " not found.");
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRates save(ExchangeRates exchangeRates) {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, exchangeRates.getBaseCurrency().getId());
            statement.setLong(2, exchangeRates.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRates.getRate());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())
                exchangeRates.setId(keys.getLong("id"));
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRates findByCodes(String baseCode, String targetCode) {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(FIND_BY_CODES)) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            var result = statement.executeQuery();
            ExchangeRates exchangeRates = null;
            if (result.next())
                exchangeRates = buildExchangeRates(result);
            return exchangeRates;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ExchangeRates> findAll() {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<ExchangeRates> exchangeRates = new ArrayList<>();
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRates(resultSet));

            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRates buildExchangeRates(ResultSet result) throws SQLException {
        CurrencyModel base = new CurrencyModel(
                result.getLong("base_id"),
                result.getString("base_code"),
                result.getString("base_name"),
                result.getString("base_sign")
        );

        CurrencyModel target = new CurrencyModel(
                result.getLong("target_id"),
                result.getString("target_code"),
                result.getString("target_name"),
                result.getString("target_sign")
        );

        ExchangeRates rate = new ExchangeRates();
        rate.setId(result.getLong("rate_id"));
        rate.setRate(result.getBigDecimal("rate"));
        rate.setBaseCurrency(base);
        rate.setTargetCurrency(target);

        return rate;
    }

    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDao() {
    }
}
