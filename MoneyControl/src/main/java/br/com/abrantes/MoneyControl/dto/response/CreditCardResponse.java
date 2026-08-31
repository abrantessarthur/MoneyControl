package br.com.abrantes.MoneyControl.dto.response;

import java.math.BigDecimal;

public record CreditCardResponse(
        Long id,
        String name,
        String lastFourDigits,
        BigDecimal creditLimit,
        Integer closingDay,
        Integer dueDay
) {
}
