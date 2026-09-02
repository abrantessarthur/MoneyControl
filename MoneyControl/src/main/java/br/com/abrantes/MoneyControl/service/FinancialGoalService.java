package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.FinancialGoalRequest;
import br.com.abrantes.MoneyControl.dto.response.FinancialGoalResponse;
import br.com.abrantes.MoneyControl.entity.FinancialGoalEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.FinancialGoalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;

    @Transactional
    public FinancialGoalResponse create(Authentication authentication, FinancialGoalRequest create) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        LocalDate today  = LocalDate.now();
        validateDate(create.finalDate());
        validateAmounts(create.amount(), create.amountToAchieve());
        FinancialGoalEntity financialGoalEntity = FinancialGoalEntity.builder()
                .description(create.description())
                .finalDate(create.finalDate())
                .initialDate(today)
                .amount(create.amount())
                .amountToAchieve(create.amountToAchieve())
                .user(user)
                .build();
        FinancialGoalEntity saved = financialGoalRepository.save(financialGoalEntity);
        return toResponse(saved);
    }

    @Transactional
    public List<FinancialGoalResponse> findAll(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return financialGoalRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FinancialGoalEntity findOwnedFinancialGoal(
            Long id,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return financialGoalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Financial Goal not found"));
    }
    @Transactional
    public void delete(Long id, Authentication authentication) {
        FinancialGoalEntity financialGoal = findOwnedFinancialGoal(id, authentication);
        financialGoalRepository.delete(financialGoal);
    }
    @Transactional
    public FinancialGoalResponse update(Authentication authentication,
                                              FinancialGoalRequest request,
                                              Long id){
        validateDate(request.finalDate());
        validateAmounts(request.amount(), request.amountToAchieve());
        FinancialGoalEntity financialGoal = findOwnedFinancialGoal(id, authentication);
        financialGoal.setDescription(request.description());
        financialGoal.setAmount(request.amount());
        financialGoal.setFinalDate(request.finalDate());
        financialGoal.setAmountToAchieve(request.amountToAchieve());
        FinancialGoalEntity saved = financialGoalRepository.save(financialGoal);
        return toResponse(saved);
    }
    private FinancialGoalResponse toResponse(FinancialGoalEntity financialGoal) {
        return new FinancialGoalResponse(
                financialGoal.getId(),
                financialGoal.getDescription(),
                financialGoal.getAmountToAchieve(),
                financialGoal.getAmount(),
                financialGoal.getFinalDate(),
                financialGoal.getInitialDate(),
                financialGoal.getAmountToAchieve().subtract(financialGoal.getAmount())
        );

    }
    private void validateDate(LocalDate finalDate) {
        LocalDate today = LocalDate.now();
        if (finalDate.isBefore(today)) {
            throw new BadRequestException("End date cannot be before now");
        }
    }
    private void validateAmounts(BigDecimal amount, BigDecimal amountToAchieve) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(
                    "Amount cannot be less than 0"
            );
        }
        if(amountToAchieve.compareTo(BigDecimal.ZERO) <= 0){
            throw new BadRequestException("Amount to achieve must be greater than zero");
        }
        if (amount.compareTo(amountToAchieve) > 0){
            throw new BadRequestException("Amount cannot be greater than amount to achieve");
        }
    }

}


