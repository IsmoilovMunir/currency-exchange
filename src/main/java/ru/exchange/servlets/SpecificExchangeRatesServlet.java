package ru.exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.exchange.dto.CurrencyDto;
import ru.exchange.dto.ExchangeRatesDto;
import ru.exchange.service.CurrenciesService;
import ru.exchange.service.ExchangeRatesService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchangeRate/*")
public class SpecificExchangeRatesServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() {
        this.exchangeRatesService = new ExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Извлекаем код валюты из URL path parameter
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.length() < 7) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"Currency code is required\"}");
                return;
            }

            // Извлекаем код валюты из /currency/USD -> USD
            String baseCode = pathInfo.substring(1,4).toUpperCase();
            String targetCode = pathInfo.substring(4).toUpperCase();
            // Вызываем сервисный метод
            ExchangeRatesDto exchangeRatesDto = exchangeRatesService.findByCode(baseCode, targetCode);

            if (exchangeRatesDto == null) {
                // Валюта не найдена
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Currency not found\"}");
            } else {
                // Возвращаем найденную валюту
                resp.setStatus(HttpServletResponse.SC_OK);
                String json = mapper.writeValueAsString(exchangeRatesDto);
                out.write(json);
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Internal server error\"}");
            e.printStackTrace();
        }
    }
}




