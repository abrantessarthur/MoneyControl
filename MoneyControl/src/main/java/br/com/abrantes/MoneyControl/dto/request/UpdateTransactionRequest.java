package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateTransactionRequest(
        @NotBlank
        String description,
        @NotNull
        @Positive
        BigDecimal amount
) {
}
