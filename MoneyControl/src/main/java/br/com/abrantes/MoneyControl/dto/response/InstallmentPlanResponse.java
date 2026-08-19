package br.com.abrantes.MoneyControl.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentPlanResponse(
        Long id,
        String description,
        Integer totalInstallments,
        LocalDate firstDueDate,
        Long categoryId,
        BigDecimal totalAmount
) {

}
