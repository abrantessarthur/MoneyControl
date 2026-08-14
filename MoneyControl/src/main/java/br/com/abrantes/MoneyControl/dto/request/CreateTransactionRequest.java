package br.com.abrantes.MoneyControl.dto.request;

import br.com.abrantes.MoneyControl.enums.TypeTransactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateTransactionRequest(
        String description,
        BigDecimal amount,
        TypeTransactional typeTransactional,
        Long categoryId,
        LocalDateTime date
) {
}