package br.com.abrantes.MoneyControl;

import br.com.abrantes.MoneyControl.dto.request.CreateTransactionRequest;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import br.com.abrantes.MoneyControl.service.TransactionalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;

    private TransactionalService transactionService;

    @Test
    @DisplayName("Não deve registrar quando não achar uma categoria")
    void naoDeveRegistrarQuandoNaoAcharUmaCategoria(){
        CreateTransactionRequest create = new CreateTransactionRequest("testando123", new BigDecimal("123400.00"), TypeTransactional.EXPENSE, 0L, LocalDateTime.parse("2026-08-17T13:18:38"));
    }

}
