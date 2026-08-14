package br.com.abrantes.MoneyControl.dto.request;

import java.math.BigDecimal;

public record UpdateTransactionRequest(
        String description,
        BigDecimal amount
) {
}
