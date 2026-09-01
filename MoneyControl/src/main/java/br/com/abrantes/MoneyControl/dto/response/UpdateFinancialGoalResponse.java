package br.com.abrantes.MoneyControl.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateFinancialGoalResponse(
        Long id,
        String description,
        BigDecimal amountToAchieve,
        BigDecimal amount,
        LocalDate finalDate,
        LocalDate initialDate,
        BigDecimal amountLeft
) {
}
