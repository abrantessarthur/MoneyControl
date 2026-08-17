package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CreateMonthlyBudgetRequest(
        @NotNull
        @Positive
        BigDecimal limitAmount,

        @NotNull
        YearMonth month,

        @NotNull
        Long categoryId
) {
}