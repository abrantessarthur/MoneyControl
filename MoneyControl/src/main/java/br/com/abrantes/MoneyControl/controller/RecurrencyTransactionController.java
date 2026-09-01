package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CreateRecurrencyTransaction;
import br.com.abrantes.MoneyControl.dto.response.RecurrencyTransactionResponse;
import br.com.abrantes.MoneyControl.service.RecurrencyTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recurrences")
@RequiredArgsConstructor
public class RecurrencyTransactionController {

    private final RecurrencyTransactionService recurrencyTransactionService;

    @PostMapping
    public ResponseEntity<RecurrencyTransactionResponse> create(
            @Valid @RequestBody CreateRecurrencyTransaction request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recurrencyTransactionService.create(authentication, request));
    }

    @GetMapping
    public List<RecurrencyTransactionResponse> findAll(Authentication authentication) {
        return recurrencyTransactionService.findAll(authentication);
    }

    @PatchMapping("/{id}/pause")
    public RecurrencyTransactionResponse pause(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return recurrencyTransactionService.pause(id, authentication);
    }

    @PatchMapping("/{id}/resume")
    public RecurrencyTransactionResponse resume(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return recurrencyTransactionService.resume(id, authentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        recurrencyTransactionService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
