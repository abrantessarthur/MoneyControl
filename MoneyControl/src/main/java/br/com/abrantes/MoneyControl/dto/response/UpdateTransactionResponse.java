package br.com.abrantes.MoneyControl.dto.response;

import br.com.abrantes.MoneyControl.enums.TypeTransactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateTransactionResponse(
        Long id,
        String description,
        BigDecimal amount,
        TypeTransactional typeTransactional,
        Long categoryId,
        String categoryName,
        LocalDateTime date
) {
}
