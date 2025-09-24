-- init.sql
-- Создание таблицы Currencies
CREATE TABLE IF NOT EXISTS Currencies (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Code VARCHAR(3) NOT NULL UNIQUE,
    FullName VARCHAR(100) NOT NULL,
    Sign VARCHAR(10) NOT NULL
);

-- Создание таблицы ExchangeRates
CREATE TABLE IF NOT EXISTS ExchangeRates (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId INTEGER NOT NULL,
    TargetCurrencyId INTEGER NOT NULL,
    Rate DECIMAL(6) NOT NULL,
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID),
    UNIQUE(BaseCurrencyId, TargetCurrencyId)
);

-- Создание индексов для ускорения поиска
CREATE UNIQUE INDEX IF NOT EXISTS idx_currencies_code ON Currencies(Code);
CREATE UNIQUE INDEX IF NOT EXISTS idx_exchange_rates_pair ON ExchangeRates(BaseCurrencyId, TargetCurrencyId);

-- Заполнение начальными данными
INSERT INTO Currencies (Code, FullName, Sign) VALUES
('USD', 'US Dollar', '$'),
('EUR', 'Euro', '€'),
('GBP', 'British Pound', '£'),
('JPY', 'Japanese Yen', '¥'),
('AUD', 'Australian Dollar', 'A$'),
('CAD', 'Canadian Dollar', 'C$'),
('CHF', 'Swiss Franc', 'Fr'),
('CNY', 'Chinese Yuan', '¥'),
('RUB', 'Russian Ruble', '₽');

-- Добавление обменных курсов
INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
(1, 2, 0.92),   -- USD to EUR
(1, 3, 0.78),   -- USD to GBP
(1, 4, 149.50), -- USD to JPY
(1, 5, 1.54),   -- USD to AUD
(2, 1, 1.09),   -- EUR to USD
(2, 3, 0.85),   -- EUR to GBP
(3, 1, 1.28);   -- GBP to USD