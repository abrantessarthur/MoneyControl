package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentPlanRequest(
        @NotBlank
        String description,
        @NotNull @Positive
        BigDecimal totalAmount,
        @NotNull @Positive @Min(2)
        Integer totalInstallments,
        @NotNull
        LocalDate firstDueDate,
        @NotNull
        Long categoryId
) {
}
