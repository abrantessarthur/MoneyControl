package br.com.abrantes.MoneyControl.dto.request;

import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.enums.RecurrenceFrequency;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateRecurrencyTransaction(
        @NotBlank
        String description,

        @NotNull
        BigDecimal amount,

        TypeTransactional typeTransactional,

        RecurrenceFrequency frequency,

        LocalDate startDate,

        LocalDate nextExecutionDate,

        LocalDate endDate,

         LocalDate lastExecutionDate,

         Boolean active,

         CategoryEntity category
) {
}
