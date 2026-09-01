package br.com.abrantes.MoneyControl;

import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.RecurringTransactionEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.enums.RecurrenceFrequency;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.RecurrencyTransactionRepository;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import br.com.abrantes.MoneyControl.service.RecurrencyTransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecurrencyTransactionServiceTest {

    @Mock
    private RecurrencyTransactionRepository recurrencyTransactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RecurrencyTransactionService service;

    @Test
    @DisplayName("Deve gerar ocorrências mensais atrasadas preservando o dia-base")
    void deveGerarOcorrenciasMensaisAtrasadas() {
        UserEntity user = UserEntity.builder().id(1L).build();
        CategoryEntity category = CategoryEntity.builder().id(2L).name("Assinaturas").build();
        RecurringTransactionEntity recurring = RecurringTransactionEntity.builder()
                .id(3L)
                .description("Serviço mensal")
                .amount(new BigDecimal("49.90"))
                .typeTransactional(TypeTransactional.EXPENSE)
                .frequency(RecurrenceFrequency.MONTHLY)
                .startDate(LocalDate.of(2026, 1, 31))
                .nextExecutionDate(LocalDate.of(2026, 1, 31))
                .active(true)
                .category(category)
                .user(user)
                .build();

        when(recurrencyTransactionRepository.findDueForUpdate(LocalDate.of(2026, 3, 31)))
                .thenReturn(List.of(recurring));
        when(transactionRepository.existsByRecurringTransactionIdAndRecurrenceReferenceDate(any(), any()))
                .thenReturn(false);

        int generated = service.processDueTransactions(LocalDate.of(2026, 3, 31));

        ArgumentCaptor<List<TransactionEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(transactionRepository).saveAll(captor.capture());

        List<TransactionEntity> transactions = captor.getValue();
        assertEquals(3, generated);
        assertEquals(LocalDate.of(2026, 1, 31), transactions.get(0).getRecurrenceReferenceDate());
        assertEquals(LocalDate.of(2026, 2, 28), transactions.get(1).getRecurrenceReferenceDate());
        assertEquals(LocalDate.of(2026, 3, 31), transactions.get(2).getRecurrenceReferenceDate());
        assertEquals(LocalDate.of(2026, 4, 30), recurring.getNextExecutionDate());
        assertEquals(LocalDate.of(2026, 3, 31), recurring.getLastExecutionDate());
        assertTrue(recurring.getActive());
    }
}
