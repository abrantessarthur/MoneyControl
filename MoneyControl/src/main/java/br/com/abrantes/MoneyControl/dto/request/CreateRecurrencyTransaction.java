package br.com.abrantes.MoneyControl.dto.request;

import br.com.abrantes.MoneyControl.enums.RecurrenceFrequency;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateRecurrencyTransaction(
        @NotBlank
        String description,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        TypeTransactional typeTransactional,

        @NotNull
        RecurrenceFrequency frequency,

        @NotNull
        LocalDate startDate,

        LocalDate endDate,

        @NotNull
        Long categoryId
) {
}
