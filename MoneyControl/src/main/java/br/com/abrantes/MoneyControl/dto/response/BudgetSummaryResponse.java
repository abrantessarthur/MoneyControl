package br.com.abrantes.MoneyControl.dto.response;

import java.math.BigDecimal;

public record BudgetSummaryResponse(
        BigDecimal limit,
        BigDecimal spent,
        BigDecimal remaining,
        BigDecimal percentageUsed
) {
}
