package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateTransactionRequest;
import br.com.abrantes.MoneyControl.dto.request.UpdateTransactionRequest;
import br.com.abrantes.MoneyControl.dto.response.TransactionResponse;
import br.com.abrantes.MoneyControl.dto.response.UpdateTransactionResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.TransactionRepository;
import br.com.abrantes.MoneyControl.repository.TransactionSummary;
import br.com.abrantes.MoneyControl.repository.TransactionsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionalService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;


    public TransactionResponse create(
            CreateTransactionRequest request,
            Authentication authentication
    ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        CategoryEntity category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        TransactionEntity transaction = TransactionEntity.builder()
                .description(request.description())
                .amount(request.amount())
                .typeTransactional(request.typeTransactional())
                .category(category)
                .user(user)
                .date(
                        request.date() != null
                                ? request.date()
                                : LocalDateTime.now()
                )
                .build();

        TransactionEntity saved = transactionRepository.save(transaction);

        return new TransactionResponse(
                saved.getId(),
                saved.getDescription(),
                saved.getAmount(),
                saved.getTypeTransactional(),
                saved.getCategory().getId(),
                saved.getCategory().getName(),
                saved.getDate()
        );
    }

    public void delete(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
        }else{
            throw new NotFoundException("Transaction not found");
        }

    }

    public UpdateTransactionResponse update(
            UpdateTransactionRequest request,
            Long id
    ) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Transaction not found")
                );

        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());

        TransactionEntity saved = transactionRepository.save(transaction);

        return new UpdateTransactionResponse(
                saved.getId(),
                saved.getDescription(),
                saved.getAmount(),
                saved.getTypeTransactional(),
                saved.getCategory().getId(),
                saved.getCategory().getName(),
                saved.getDate()
        );
    }

    public Page<TransactionsProjection> getTransactionsPage(Integer page, Integer size){
        return transactionRepository.getTransactionsPage(PageRequest.of(page, size));
    }

    public Optional<TransactionResponse> getMostExpensiveTransaction() {
        return transactionRepository.findMostExpensiveTransaction(PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }

    public BigDecimal getBalance(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return transactionRepository.calculateBalance(user.getId());
    }

    public TransactionSummary getSumary(){
        return transactionRepository.getSumary();
    }


}
