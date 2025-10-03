package ru.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyModel {
    private Long id;
    private String code;
    private String fullName;
    private String sign;
}
