package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateCreditCard (
        @NotBlank
        String name,
        @NotBlank
        @Pattern(regexp = "\\d{4}", message = "lastFourDigits must contain exactly 4 digits")
        String lastFourDigits,
        @NotNull
        @Positive
        BigDecimal creditLimit,
        @NotNull
        @Min(1)
        @Max(31)
        Integer closingDay,
        @NotNull
        @Min(1)
        @Max(31)
        Integer dueDay
){
}
