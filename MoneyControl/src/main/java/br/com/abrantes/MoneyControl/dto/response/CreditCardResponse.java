package br.com.abrantes.MoneyControl.dto.response;

public record CreditCardResponse(
        Long id,
        String lastFourDigits,
        String creditLimit,
        Integer closingDay,
        Integer dueDay
) {
}
