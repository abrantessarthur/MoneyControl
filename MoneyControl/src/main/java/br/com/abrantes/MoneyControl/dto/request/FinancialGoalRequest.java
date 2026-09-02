package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialGoalRequest(
        @NotBlank
        String description,

        @NotNull
        BigDecimal amountToAchieve,

        @NotNull
        BigDecimal amount,
        @NotNull
        LocalDate finalDate
) {
}
