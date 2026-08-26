package br.com.abrantes.MoneyControl.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateCreditCard (
        @NotBlank
        String name,
        @NotBlank
        String lastFourDigits,
        @NotBlank
        BigDecimal creditLimit,
        @NotBlank
        Integer closingDay,
        @NotBlank
        Integer dueDay
){
}
