package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentPlanRequest(
        String description,
        @NotNull
        BigDecimal totalAmount,
        @NotNull
        Integer totalInstallments,
        @NotBlank
        LocalDate firstDueDate,
        @NotNull
        Long categoryId
) {
}
