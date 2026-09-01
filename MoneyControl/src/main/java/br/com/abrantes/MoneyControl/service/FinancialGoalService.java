package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateFinancialGoal;
import br.com.abrantes.MoneyControl.dto.request.UpdateFinancialGoalRequest;
import br.com.abrantes.MoneyControl.dto.response.FinancialGoalResponse;
import br.com.abrantes.MoneyControl.dto.response.UpdateFinancialGoalResponse;
import br.com.abrantes.MoneyControl.entity.FinancialGoalEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.FinancialGoalRepository;
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

    public FinancialGoalResponse create(Authentication authentication, CreateFinancialGoal create) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        LocalDate today = LocalDate.now();

        if (create.finalDate() != null && create.finalDate().isBefore(today)) {
            throw new BadRequestException("End date cannot be before now");
        }

        if (create.amount().compareTo(BigDecimal.ZERO) <= 0 || create.amountToAchieve().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Amount must be greater than zero"
            );
        }
        if (create.amount().compareTo(create.amountToAchieve()) >= 0) {
            throw new BadRequestException("The target amount must be greater than the current amount.");
        }

        FinancialGoalEntity financialGoalEntity = FinancialGoalEntity.builder()
                .description(create.description())
                .finalDate(create.finalDate())
                .initialDate(LocalDate.now())
                .amount(create.amount())
                .amountToAchieve(create.amountToAchieve())
                .user(user)
                .build();

        FinancialGoalEntity saved = financialGoalRepository.save(financialGoalEntity);

        return toResponse(saved);

    }

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

    public void delete(Long id, Authentication authentication) {
        FinancialGoalEntity financialGoal = findOwnedFinancialGoal(id, authentication);
        financialGoalRepository.delete(financialGoal);
    }

    public UpdateFinancialGoalResponse update(Authentication authentication,
                                              UpdateFinancialGoalRequest request,
                                              Long id){
        if (request.finalDate() != null && request.finalDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("End date cannot be before now");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0 || request.amountToAchieve().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Amount must be greater than zero"
            );
        }
        if (request.amount().compareTo(request.amountToAchieve()) >= 0) {
            throw new BadRequestException("The target amount must be greater than the current amount.");
        }
        FinancialGoalEntity financial = findOwnedFinancialGoal(id, authentication);
        FinancialGoalEntity financialGoal = financialGoalRepository.findById(id).orElseThrow(() -> new NotFoundException("Financial Goal not found"));
        financialGoal.setDescription(request.description());
        financialGoal.setAmount(request.amount());
        financialGoal.setFinalDate(request.finalDate());
        financialGoal.setAmountToAchieve(request.amountToAchieve());
        FinancialGoalEntity saved = financialGoalRepository.save(financialGoal);
        return new UpdateFinancialGoalResponse(
                saved.getId(),
                saved.getDescription(),
                saved.getAmountToAchieve(),
                saved.getAmount(),
                saved.getFinalDate(),
                saved.getInitialDate(),
                saved.getAmountToAchieve().subtract(saved.getAmount())
        );
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
}


