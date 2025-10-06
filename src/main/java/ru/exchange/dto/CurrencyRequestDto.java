package ru.exchange.dto;

import lombok.Data;

@Data
public class CurrencyRequestDto {
    private Long id;
    private String code;
    private String fullName;
    private String sign;
}
