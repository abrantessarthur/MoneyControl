package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateFinancialGoalRequest(
        @NotBlank
        String description,
        @NotNull
        BigDecimal amount,
        @NotNull
        BigDecimal amountToAchieve,
        @NotNull
        LocalDate finalDate
) {
}
