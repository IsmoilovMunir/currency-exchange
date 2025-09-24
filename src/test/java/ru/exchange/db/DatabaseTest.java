package ru.exchange.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // Тест подключения
            Connection conn = DatabaseManager.getConnection();
            System.out.println("✅ Database connection successful!");

            // Тест чтения данных
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Currencies")) {

                System.out.println("✅ Currencies table content:");
                while (rs.next()) {
                    System.out.printf("ID: %d, Code: %s, Name: %s, Sign: %s%n",
                            rs.getInt("ID"),
                            rs.getString("Code"),
                            rs.getString("FullName"),
                            rs.getString("Sign"));
                }
            }

            // Тест обменных курсов
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT c1.Code as Base, c2.Code as Target, er.Rate " +
                                 "FROM ExchangeRates er " +
                                 "JOIN Currencies c1 ON er.BaseCurrencyId = c1.ID " +
                                 "JOIN Currencies c2 ON er.TargetCurrencyId = c2.ID")) {

                System.out.println("✅ Exchange rates:");
                while (rs.next()) {
                    System.out.printf("%s/%s: %.4f%n",
                            rs.getString("Base"),
                            rs.getString("Target"),
                            rs.getDouble("Rate"));
                }
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
