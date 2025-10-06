package ru.exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.exchange.dto.CurrencyDto;
import ru.exchange.service.CurrenciesService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class SpecificCurrencyServlet extends HttpServlet {
    private CurrenciesService currencyService;
    @Override
    public void init() {
        this.currencyService = new CurrenciesService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Извлекаем код валюты из URL path parameter
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"Currency code is required\"}");
                return;
            }

            // Извлекаем код валюты из /currency/USD -> USD
            String code = pathInfo.substring(1).toUpperCase();

            // Вызываем сервисный метод
            CurrencyDto currency = currencyService.findByCode(code);

            if (currency == null) {
                // Валюта не найдена
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Currency not found\"}");
            } else {
                // Возвращаем найденную валюту
                response.setStatus(HttpServletResponse.SC_OK);
                String json = mapper.writeValueAsString(currency);
                out.write(json);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"Internal server error\"}");
            e.printStackTrace();
        }
    }

}
