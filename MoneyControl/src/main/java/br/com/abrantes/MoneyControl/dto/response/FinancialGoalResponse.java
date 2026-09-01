package br.com.abrantes.MoneyControl.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialGoalResponse(
        Long id,
        String description,
        BigDecimal amountToAchive,
        BigDecimal amount,
        LocalDate finalDate,
        LocalDate currentDate
) {
}
