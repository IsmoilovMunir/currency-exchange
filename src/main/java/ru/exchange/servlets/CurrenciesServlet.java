package ru.exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.exchange.dto.CurrencyDto;
import ru.exchange.dto.CurrencyRequestDto;
import ru.exchange.dto.CurrencyResponseDto;
import ru.exchange.service.CurrenciesService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/currency")
public class CurrenciesServlet extends HttpServlet {
    private final CurrenciesService currenciesService = CurrenciesService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            // Получаем список всех валют
            List<CurrencyDto> currencies = currenciesService.findAll();

            // Преобразуем в JSON и отправляем
            String jsonResponse = mapper.writeValueAsString(currencies);
            resp.getWriter().write(jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            // В случае ошибки возвращаем 500
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Database unavailable\"}");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        CurrencyRequestDto dto = mapper.readValue(req.getInputStream(), CurrencyRequestDto.class);


        // Читаем JSON из тела запроса → в объект CurrencyModel

        // Проверяем обязательные поля
        if (dto.getName() == null || dto.getCode() == null || dto.getSign() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Missing required parameters\"}");
            return;
        }

        // Сохраняем в БД
        CurrencyResponseDto responseDto = currenciesService.save(dto);

        // Возвращаем JSON с созданной записью
        mapper.writeValue(resp.getWriter(), responseDto);
    }

}
