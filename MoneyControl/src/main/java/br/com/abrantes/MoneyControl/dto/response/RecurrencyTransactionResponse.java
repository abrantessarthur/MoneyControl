package br.com.abrantes.MoneyControl.dto.response;

import br.com.abrantes.MoneyControl.enums.RecurrenceFrequency;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecurrencyTransactionResponse(
        String description,
        BigDecimal amount,
        TypeTransactional typeTransactional,
        RecurrenceFrequency frequency,
        LocalDate startDate,
        LocalDate nextExecutionDate,
        LocalDate endDate,
        LocalDate lastExecutionDate,
        Boolean active,
        Long categoryId
        ) {
}
