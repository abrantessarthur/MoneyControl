package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateMonthlyBudgetRequest;
import br.com.abrantes.MoneyControl.dto.response.BudgetSummaryResponse;
import br.com.abrantes.MoneyControl.dto.response.MonthlyBudgetResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.CreditCardEntity;
import br.com.abrantes.MoneyControl.entity.MonthlyBudgetEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.MonthlyBudgetRepository;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MonthlyBudgetService {

    private final MonthlyBudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public MonthlyBudgetResponse create(
            CreateMonthlyBudgetRequest request,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        CategoryEntity category = categoryRepository
                .findByIdAndUserId(request.categoryId(), user.getId())
                .orElseThrow(() ->
                        new NotFoundException("Category not found")
                );
        MonthlyBudgetEntity budget = MonthlyBudgetEntity.builder()
                .limitAmount(request.limitAmount())
                .month(request.month())
                .category(category)
                .user(user)
                .build();

        MonthlyBudgetEntity saved = budgetRepository.save(budget);

        return new MonthlyBudgetResponse(
                saved.getId(),
                saved.getLimitAmount(),
                saved.getMonth(),
                saved.getCategory().getId(),
                saved.getCategory().getName()
        );
    }

    public BudgetSummaryResponse getSummary(
            Long budgetId,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        MonthlyBudgetEntity budget = budgetRepository
                .findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() ->
                        new NotFoundException("Budget not found")
                );

        LocalDateTime startDate = budget.getMonth()
                .atDay(1)
                .atStartOfDay();

        LocalDateTime endDate = budget.getMonth()
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay();

        BigDecimal spent = transactionRepository.calculateSpent(
                user.getId(),
                budget.getCategory().getId(),
                startDate,
                endDate
        );

        BigDecimal limit = budget.getLimitAmount();
        BigDecimal remaining = limit.subtract(spent);

        BigDecimal percentageUsed = BigDecimal.ZERO;

        if (limit.compareTo(BigDecimal.ZERO) > 0) {
            percentageUsed = spent
                    .multiply(BigDecimal.valueOf(100))
                    .divide(limit, 2, RoundingMode.HALF_UP);
        }

        return new BudgetSummaryResponse(
                limit,
                spent,
                remaining,
                percentageUsed
        );
    }

    public void delete(Long id, Authentication authentication) {
        MonthlyBudgetEntity monthlyBudgetEntity = findOwnedMonthlyBudget(id, authentication);
        budgetRepository.delete(monthlyBudgetEntity);
    }

    private MonthlyBudgetEntity findOwnedMonthlyBudget(Long id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Monthly Budget not found"));
    }
}
