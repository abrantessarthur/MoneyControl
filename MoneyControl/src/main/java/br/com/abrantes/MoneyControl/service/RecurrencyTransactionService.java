package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateRecurrencyTransaction;
import br.com.abrantes.MoneyControl.dto.response.RecurrencyTransactionResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.RecurringTransactionEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.RecurrencyTransactionRepository;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurrencyTransactionService {

    private static final int MAX_OCCURRENCES_PER_RUN = 1_000;

    private final RecurrencyTransactionRepository recurrencyTransactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public RecurrencyTransactionResponse create(
            Authentication authentication,
            CreateRecurrencyTransaction request
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        CategoryEntity category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (request.endDate() != null && request.endDate().isBefore(request.startDate())) {
            throw new BadRequestException("End date cannot be before now");
        }

        RecurringTransactionEntity recurring = RecurringTransactionEntity.builder()
                .description(request.description())
                .amount(request.amount())
                .typeTransactional(request.typeTransactional())
                .frequency(request.frequency())
                .startDate(request.startDate())
                .nextExecutionDate(request.startDate())
                .endDate(request.endDate())
                .active(true)
                .category(category)
                .user(user)
                .build();

        RecurringTransactionEntity saved = recurrencyTransactionRepository.save(recurring);

        // Recorrências que começam hoje ou no passado são lançadas imediatamente.
        generateDueOccurrences(saved, LocalDate.now());
        recurrencyTransactionRepository.save(saved);

        return toResponse(saved);
    }

    @Transactional
    public int processDueTransactions(LocalDate referenceDate) {
        List<RecurringTransactionEntity> dueTransactions =
                recurrencyTransactionRepository.findDueForUpdate(referenceDate);

        int generatedTransactions = 0;
        for (RecurringTransactionEntity recurring : dueTransactions) {
            generatedTransactions += generateDueOccurrences(recurring, referenceDate);
        }

        recurrencyTransactionRepository.saveAll(dueTransactions);
        return generatedTransactions;
    }

    public List<RecurrencyTransactionResponse> findAll(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return recurrencyTransactionRepository
                .findAllByUserIdOrderByNextExecutionDateAsc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RecurrencyTransactionResponse pause(Long id, Authentication authentication) {
        RecurringTransactionEntity recurring = findOwnedRecurrence(id, authentication);
        recurring.setActive(false);
        return toResponse(recurrencyTransactionRepository.save(recurring));
    }

    @Transactional
    public RecurrencyTransactionResponse resume(Long id, Authentication authentication) {
        RecurringTransactionEntity recurring = findOwnedRecurrence(id, authentication);

        if (recurring.getEndDate() != null
                && recurring.getNextExecutionDate().isAfter(recurring.getEndDate())) {
            throw new BadRequestException("Recurrence has already reached its end date");
        }

        recurring.setActive(true);
        return toResponse(recurrencyTransactionRepository.save(recurring));
    }

    @Transactional
    public void delete(Long id, Authentication authentication) {
        RecurringTransactionEntity recurring = findOwnedRecurrence(id, authentication);
        recurrencyTransactionRepository.delete(recurring);
    }

    private int generateDueOccurrences(
            RecurringTransactionEntity recurring,
            LocalDate referenceDate
    ) {
        if (!Boolean.TRUE.equals(recurring.getActive())) {
            return 0;
        }

        List<TransactionEntity> generated = new ArrayList<>();
        int processedOccurrences = 0;

        while (isDue(recurring, referenceDate)
                && processedOccurrences < MAX_OCCURRENCES_PER_RUN) {
            LocalDate occurrenceDate = recurring.getNextExecutionDate();

            boolean alreadyGenerated = transactionRepository
                    .existsByRecurringTransactionIdAndRecurrenceReferenceDate(
                            recurring.getId(),
                            occurrenceDate
                    );

            if (!alreadyGenerated) {
                generated.add(TransactionEntity.builder()
                        .description(recurring.getDescription())
                        .amount(recurring.getAmount())
                        .typeTransactional(recurring.getTypeTransactional())
                        .category(recurring.getCategory())
                        .user(recurring.getUser())
                        .date(occurrenceDate.atStartOfDay())
                        .recurringTransaction(recurring)
                        .recurrenceReferenceDate(occurrenceDate)
                        .build());
            }

            recurring.setLastExecutionDate(occurrenceDate);
            LocalDate nextExecutionDate = nextExecutionDate(recurring, occurrenceDate);
            recurring.setNextExecutionDate(nextExecutionDate);

            if (recurring.getEndDate() != null
                    && nextExecutionDate.isAfter(recurring.getEndDate())) {
                recurring.setActive(false);
            }

            processedOccurrences++;
        }

        if (!generated.isEmpty()) {
            transactionRepository.saveAll(generated);
        }

        return generated.size();
    }

    private boolean isDue(RecurringTransactionEntity recurring, LocalDate referenceDate) {
        LocalDate nextExecutionDate = recurring.getNextExecutionDate();
        return Boolean.TRUE.equals(recurring.getActive())
                && nextExecutionDate != null
                && !nextExecutionDate.isAfter(referenceDate)
                && (recurring.getEndDate() == null
                || !nextExecutionDate.isAfter(recurring.getEndDate()));
    }

    private LocalDate nextExecutionDate(
            RecurringTransactionEntity recurring,
            LocalDate currentExecutionDate
    ) {
        return switch (recurring.getFrequency()) {
            case DAILY -> currentExecutionDate.plusDays(1);
            case WEEKLY -> currentExecutionDate.plusWeeks(1);
            case MONTHLY -> nextMonthlyDate(recurring.getStartDate(), currentExecutionDate);
            case YEARLY -> nextYearlyDate(recurring.getStartDate(), currentExecutionDate);
        };
    }

    private LocalDate nextMonthlyDate(LocalDate startDate, LocalDate currentDate) {
        YearMonth nextMonth = YearMonth.from(currentDate).plusMonths(1);
        int day = Math.min(startDate.getDayOfMonth(), nextMonth.lengthOfMonth());
        return nextMonth.atDay(day);
    }

    private LocalDate nextYearlyDate(LocalDate startDate, LocalDate currentDate) {
        int nextYear = currentDate.getYear() + 1;
        Month month = startDate.getMonth();
        int day = Math.min(
                startDate.getDayOfMonth(),
                YearMonth.of(nextYear, month).lengthOfMonth()
        );
        return LocalDate.of(nextYear, month, day);
    }

    private RecurringTransactionEntity findOwnedRecurrence(
            Long id,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return recurrencyTransactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Recurring transaction not found"));
    }

    private RecurrencyTransactionResponse toResponse(RecurringTransactionEntity recurring) {
        return new RecurrencyTransactionResponse(
                recurring.getId(),
                recurring.getDescription(),
                recurring.getAmount(),
                recurring.getTypeTransactional(),
                recurring.getFrequency(),
                recurring.getStartDate(),
                recurring.getNextExecutionDate(),
                recurring.getEndDate(),
                recurring.getLastExecutionDate(),
                recurring.getActive(),
                recurring.getCategory().getId()
        );
    }
}
