package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.InstallmentPlanRequest;
import br.com.abrantes.MoneyControl.dto.response.InstallmentPlanResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.InstallmentPlanEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.InstallmenPlanProjection;
import br.com.abrantes.MoneyControl.repository.InstallmentPlanRepository;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstallmentPlanService {
    private final InstallmentPlanRepository installmentPlanRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public InstallmentPlanResponse create(
            InstallmentPlanRequest request,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        CategoryEntity category = categoryRepository
                .findByIdAndUserId(request.categoryId(), user.getId())
                .orElseThrow(() ->
                        new NotFoundException("Category not found")
                );

        if (request.totalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(
                    "Total amount must be greater than zero"
            );
        }

        if (request.totalInstallments() <= 1) {
            throw new BadRequestException(
                    "Total installments must be greater than one"
            );
        }

        InstallmentPlanEntity plan = InstallmentPlanEntity.builder()
                .description(request.description())
                .totalAmount(request.totalAmount())
                .totalInstallments(request.totalInstallments())
                .firstDueDate(request.firstDueDate())
                .category(category)
                .user(user)
                .build();

        InstallmentPlanEntity savedPlan =
                installmentPlanRepository.save(plan);

        BigDecimal installmentAmount = request.totalAmount()
                .divide(
                        BigDecimal.valueOf(request.totalInstallments()),
                        2,
                        RoundingMode.DOWN
                );

        List<TransactionEntity> transactions = new ArrayList<>();

        for (int index = 0; index < request.totalInstallments(); index++) {
            int installmentNumber = index + 1;

            // A última parcela recebe a diferença do arredondamento.
            BigDecimal amount = installmentNumber == request.totalInstallments()
                    ? request.totalAmount().subtract(
                    installmentAmount.multiply(
                            BigDecimal.valueOf(
                                    request.totalInstallments() - 1L
                            )
                    )
            )
                    : installmentAmount;

            TransactionEntity transaction = TransactionEntity.builder()
                    .description(
                            request.description()
                                    + " "
                                    + installmentNumber
                                    + "/"
                                    + request.totalInstallments()
                    )
                    .amount(amount)
                    .typeTransactional(TypeTransactional.EXPENSE)
                    .date(
                            request.firstDueDate()
                                    .plusMonths(index)
                                    .atStartOfDay()
                    )
                    .category(category)
                    .user(user)
                    .installmentPlan(savedPlan)
                    .installmentNumber(installmentNumber)
                    .build();

            transactions.add(transaction);
        }

        transactionRepository.saveAll(transactions);

        return new InstallmentPlanResponse(
                savedPlan.getId(),
                savedPlan.getDescription(),
                savedPlan.getTotalInstallments(),
                savedPlan.getFirstDueDate(),
                savedPlan.getCategory().getId(),
                savedPlan.getTotalAmount()
        );
    }

    @Transactional
    public void delete(Long id, Authentication authentication){
        UserEntity user = (UserEntity) authentication.getPrincipal();
        InstallmentPlanEntity plan = installmentPlanRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("InstallmentPlan not found"));
        installmentPlanRepository.delete(plan);
    }

    public Page<InstallmenPlanProjection> getInstallmentPage(Integer page, Integer size, Authentication authentication){
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return installmentPlanRepository.getInstallmentsPage(user.getId(), PageRequest.of(page, size));
    }
}
