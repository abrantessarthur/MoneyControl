package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateTransactionRequest(
        @NotBlank
        String description,
        @NotNull
        BigDecimal amount
) {
}
