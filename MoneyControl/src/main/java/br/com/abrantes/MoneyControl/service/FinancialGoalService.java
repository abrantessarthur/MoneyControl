package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateFinancialGoal;
import br.com.abrantes.MoneyControl.dto.response.FinancialGoalResponse;
import br.com.abrantes.MoneyControl.dto.response.RecurrencyTransactionResponse;
import br.com.abrantes.MoneyControl.entity.FinancialGoalEntity;
import br.com.abrantes.MoneyControl.entity.RecurringTransactionEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.FinancialGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;

    public FinancialGoalResponse create(Authentication authentication, CreateFinancialGoal create) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (create.finalDate() != null && create.finalDate().isBefore(create.initialDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        FinancialGoalEntity financialGoalEntity = FinancialGoalEntity.builder()
                .description(create.description())
                .finalDate(create.finalDate())
                .initialDate(LocalDate.now())
                .amount(create.amount())
                .amountToAchive(create.amountToAchive())
                .user(user)
                .build();

        FinancialGoalEntity saved = financialGoalRepository.save(financialGoalEntity);

        return toResponse(saved);

    }

    public List<FinancialGoalResponse> findAll(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return financialGoalRepository
                .findAll
    }

    public void delete(Long id){
        if(financialGoalRepository.existsById(id)){
            financialGoalRepository.deleteById(id);
        }else {
            throw new NotFoundException("Financial goal not found");
        }
    }
    private FinancialGoalResponse toResponse(FinancialGoalEntity financialGoal) {
        return new FinancialGoalResponse(
                financialGoal.getId(),
                financialGoal.getDescription(),
                financialGoal.getAmountToAchive(),
                financialGoal.getAmount(),
                financialGoal.getFinalDate(),
                financialGoal.getInitialDate(),
                financialGoal.getAmountLeft()
        );
    }

}
