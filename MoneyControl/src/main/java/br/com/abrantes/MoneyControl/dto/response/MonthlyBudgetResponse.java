package br.com.abrantes.MoneyControl.dto.response;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyBudgetResponse(
        Long id,
        BigDecimal limitAmount,
        YearMonth month,
        Long categoryId,
        String categoryName
) {
}
