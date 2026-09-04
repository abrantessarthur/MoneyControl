package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateCreditCard;
import br.com.abrantes.MoneyControl.dto.response.CreditCardResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.CreditCardEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CreditCardRepository;
import jakarta.transaction.Transactional;
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
            throw new BadRequestException("Credit limit must be greater than 0");
        }
        CreditCardEntity creditCard= CreditCardEntity.builder()
                .creditLimit(request.creditLimit())
                .name(request.name())
                .dueDay(request.dueDay())
                .closingDay(request.closingDay())
                .lastFourDigits(request.lastFourDigits())
                .user(user)
                .build();
        CreditCardEntity saved = creditCardRepository.save(creditCard);
        return new CreditCardResponse(
                saved.getId(),
                saved.getName(),
                saved.getLastFourDigits(),
                saved.getCreditLimit(),
                saved.getClosingDay(),
                saved.getDueDay()
        );
    }

    @Transactional
    public void delete(Long id, Authentication authentication){
        CreditCardEntity creditCard = findOwnedCreditCard(id, authentication);
        creditCardRepository.delete(creditCard);

    }
    private CreditCardEntity findOwnedCreditCard(Long id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return creditCardRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Credit Card not found"));
    }


}
