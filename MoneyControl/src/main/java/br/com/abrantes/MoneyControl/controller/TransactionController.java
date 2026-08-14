package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CreateTransactionRequest;
import br.com.abrantes.MoneyControl.dto.request.UpdateTransactionRequest;
import br.com.abrantes.MoneyControl.dto.response.TransactionResponse;
import br.com.abrantes.MoneyControl.dto.response.UpdateTransactionResponse;
import br.com.abrantes.MoneyControl.repository.TransactionsProjection;
import br.com.abrantes.MoneyControl.service.TransactionalService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionalService transactionService;

    @PostMapping
    @Transactional
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        TransactionResponse created = transactionService.create(createTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<UpdateTransactionResponse>  updateTransaction(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest) {
        UpdateTransactionResponse updated = transactionService.update(updateTransactionRequest, id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<TransactionsProjection> getAllTransactionsPageable(@PathVariable Integer page, @PathVariable Integer size){
        return transactionService.getTransactionsPage(page, size);
    }

    @GetMapping("/expensive")
    public ResponseEntity<TransactionResponse> getMostExpensiveTransaction(){
        return transactionService.getMostExpensiveTransaction()
                .map(transaction -> ResponseEntity.ok(transaction))
                .orElse(ResponseEntity.noContent().build());
    }
}
