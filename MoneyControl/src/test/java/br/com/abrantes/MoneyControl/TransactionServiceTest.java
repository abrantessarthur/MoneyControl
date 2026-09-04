package br.com.abrantes.MoneyControl;

import br.com.abrantes.MoneyControl.dto.request.CreateTransactionRequest;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import br.com.abrantes.MoneyControl.service.TransactionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private Authentication authentication;

    private TransactionalService transactionService;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionalService(
                transactionRepository,
                categoryRepository
        );
        user = UserEntity.builder().id(10L).build();
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    @DisplayName("Não deve registrar quando não achar uma categoria")
    void naoDeveRegistrarQuandoNaoAcharUmaCategoria(){
        CreateTransactionRequest create = new CreateTransactionRequest("testando123", new BigDecimal("123400.00"), TypeTransactional.EXPENSE, 0L, LocalDateTime.parse("2026-08-17T13:18:38"));

        when(categoryRepository.findByIdAndUserId(0L, user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> transactionService.create(create, authentication)
        );

        assertEquals("Category not found", exception.getMessage());
        verify(transactionRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("transaction must be greater than zero ")
    void mustBeGreaterThanZero(){
        CreateTransactionRequest create = new CreateTransactionRequest("testando123", new BigDecimal("0"), TypeTransactional.EXPENSE, 1L, LocalDateTime.parse("2026-08-17T13:18:38"));
        CategoryEntity category = CategoryEntity.builder()
                .id(1L)
                .name("Test category")
                .build();

        when(categoryRepository.findByIdAndUserId(1L, user.getId())).thenReturn(Optional.of(category));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> transactionService.create(create, authentication)
        );
        assertEquals("Amount must be greater than zero", exception.getMessage());
        verify(transactionRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

}
