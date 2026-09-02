package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CreateTransactionRequest;
import br.com.abrantes.MoneyControl.dto.request.UpdateTransactionRequest;
import br.com.abrantes.MoneyControl.dto.response.TransactionResponse;
import br.com.abrantes.MoneyControl.dto.response.UpdateTransactionResponse;
import br.com.abrantes.MoneyControl.repository.TransactionSummary;
import br.com.abrantes.MoneyControl.repository.TransactionsProjection;
import br.com.abrantes.MoneyControl.service.TransactionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionalService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest,
            Authentication authentication
    ) {
        TransactionResponse created = transactionService.create(
                createTransactionRequest,
                authentication
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, Authentication authentication) {
        transactionService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdateTransactionResponse>  updateTransaction(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest,
                                                                        Authentication authentication) {
        UpdateTransactionResponse updated = transactionService.update(updateTransactionRequest, id, authentication);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<TransactionsProjection> getAllTransactionsPageable(@PathVariable Integer page, @PathVariable Integer size, Authentication authentication) {
        return transactionService.getTransactionsPage(page, size, authentication);
    }

    @GetMapping("/expensive")
    public ResponseEntity<TransactionResponse> getMostExpensiveTransaction(Authentication authentication){
        return transactionService.getMostExpensiveTransaction(authentication)
                .map(transaction -> ResponseEntity.ok(transaction))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                transactionService.getBalance(authentication)
        );
    }

    @GetMapping("/sumary")
    public TransactionSummary getSumary(Authentication authentication){
        return transactionService.getSumary(authentication);
    }

}
