package ru.exchange.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/db/exchange.db";
    private static boolean isInitialized = false;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);

        // Включаем поддержку внешних ключей (важно для SQLite)
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }

    private static void initializeDatabase() {
        if (isInitialized) return;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Проверяем, существуют ли уже таблицы
            boolean tablesExist = checkIfTablesExist(stmt);

            if (!tablesExist) {
                // Выполняем скрипт инициализации
                executeInitScript(conn);
                System.out.println("Database initialized successfully");
            } else {
                System.out.println("Database already exists, skipping initialization");
            }

            isInitialized = true;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static boolean checkIfTablesExist(Statement stmt) {
        try {
            // Пытаемся выполнить запрос к таблице Currencies
            stmt.executeQuery("SELECT 1 FROM Currencies LIMIT 1");
            stmt.executeQuery("SELECT 1 FROM ExchangeRates LIMIT 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static void executeInitScript(Connection conn) {
        try (InputStream is = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("db/init.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            StringBuilder script = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // Пропускаем комментарии и пустые строки
                if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                    continue;
                }
                script.append(line).append("\n");

                // Если строка заканчивается на ;, выполняем этот SQL
                if (line.trim().endsWith(";")) {
                    String sql = script.toString().trim();
                    if (!sql.isEmpty()) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(sql);
                        }
                    }
                    script.setLength(0); // Очищаем StringBuilder
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute initialization script", e);
        }
    }
}
