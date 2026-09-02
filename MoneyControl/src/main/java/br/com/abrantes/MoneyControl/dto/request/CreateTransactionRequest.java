package br.com.abrantes.MoneyControl.dto.request;

import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateTransactionRequest(
        @NotBlank
        String description,
        @NotNull
        BigDecimal amount,
        @NotBlank
        TypeTransactional typeTransactional,
        @NotNull
        Long categoryId,
        @NotBlank
        LocalDateTime date
) {
}