package ru.exchange.dao;

import ru.exchange.db.utils.ConnectionManger;
import ru.exchange.model.ExchangeRates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ExchangeRatesDao {
    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();
    private static final String FIND_ALL = """
            select id, base_currency_id, target_currency_id, rate
            from exchange_rates
            """;
    private static final String FIND_BY_CODES = FIND_ALL + """
            WHERE base_currency_id = ?::INTEGER AND target_currency_id = ?::INTEGER
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
        try (var connection = ConnectionManger.open();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setLong(1, exchangeRates.getBaseCurrencyId());
            statement.setLong(2, exchangeRates.getTargetCurrencyId());
            statement.setDouble(3, exchangeRates.getRate());
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
        try (var connection = ConnectionManger.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, exchangeRates.getBaseCurrencyId());
            statement.setLong(2, exchangeRates.getTargetCurrencyId());
            statement.setDouble(3, exchangeRates.getRate());
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
        try (var connection = ConnectionManger.open();
             var statement = connection.prepareStatement(FIND_BY_CODES)) {
            statement.setInt(1, Integer.parseInt(baseCode));
            statement.setInt(2, Integer.parseInt(targetCode));
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
        try (var connection = ConnectionManger.open();
             var statement = connection.prepareStatement(FIND_ALL)) {
            List<ExchangeRates> exchangeRates = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                exchangeRates.add(buildExchangeRates(result)
                );
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRates buildExchangeRates(ResultSet result) throws SQLException {
        return new ExchangeRates(
                result.getLong("id"),
                result.getLong("base_currency_id"),
                result.getLong("target_currency_id"),
                result.getDouble("rate"));
    }

    public static ExchangeRatesDao getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDao() {
    }
}
