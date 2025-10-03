package ru.exchange.model;

import lombok.Data;

@Data
public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private String sign;
}
