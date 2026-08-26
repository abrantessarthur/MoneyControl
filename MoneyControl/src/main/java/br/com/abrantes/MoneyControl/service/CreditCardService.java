package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateCreditCard;
import br.com.abrantes.MoneyControl.dto.response.CreditCardResponse;
import br.com.abrantes.MoneyControl.entity.CreditCardEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;

    public CreditCardResponse create(CreateCreditCard request, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        if(request.creditLimit().compareTo(BigDecimal.ZERO) <= 0 ){
            throw new IllegalArgumentException("Credit limit must be greater than 0");
        }
        CreditCardEntity creditCard= CreditCardEntity.builder()
                .creditLimit(request.creditLimit())
                .name(request.name())
                .dueDay(request.dueDay())
                .closingDay(request.closingDay())
                .lastFourDigits(request.lastFourDigits())
                .build();
        CreditCardEntity saved = creditCardRepository.save(creditCard);
        return new CreditCardResponse(
                saved.getId(),
                saved.getLastFourDigits(),
                saved.getName(),
                saved.getClosingDay(),
                saved.getDueDay()
        );
    }
}
