package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateFinancialGoal(
        @NotBlank
        String description,

        @NotNull
        BigDecimal amountToAchive,

        @NotNull
        BigDecimal amount,

        LocalDate finalDate,

        LocalDate initialDate
) {
}
