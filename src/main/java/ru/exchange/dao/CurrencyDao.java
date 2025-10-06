package ru.exchange.dao;

import ru.exchange.db.utils.ConnectionManger;
import ru.exchange.model.CurrencyModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    private static final CurrencyDao INSTANCE = new CurrencyDao();
    private static final String FIND_ALL_SQL = """
            SELECT id,code,full_name,sign
            FROM currencies;
            """;
    private static final String FIND_BY_CODE = """
            SELECT id, code, full_name, sign
            FROM currencies
            WHERE code = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO currencies 
            (code, full_name, sign)
            VALUES (?, ?, ?);
            """;

    public List<CurrencyModel> findAll() {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<CurrencyModel> currencies = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                currencies.add(build(result));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException("Database error in findAll", e);
        }
    }

    public CurrencyModel findByCode(String code) {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(FIND_BY_CODE)) {
            statement.setString(1, code);
            var result = statement.executeQuery();

            CurrencyModel currencyModel = null;

            if (result.next()) {
                currencyModel = build(result);
            }

            return currencyModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrencyModel save(CurrencyModel currencyModel) {
        try (var connection = ConnectionManger.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, currencyModel.getCode());
            statement.setString(2, currencyModel.getFullName());
            statement.setString(3, currencyModel.getSign());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())
                currencyModel.setId(keys.getLong("id"));
            return currencyModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrencyModel build(ResultSet result) throws SQLException {
        CurrencyModel currency = new CurrencyModel();
        currency.setId(result.getLong("id"));
        currency.setCode(result.getString("code"));
        currency.setFullName(result.getString("full_name"));
        currency.setSign(result.getString("sign"));
        return currency;
    }


    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    public CurrencyDao() {
    }
}
