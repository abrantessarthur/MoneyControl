package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CreateMonthlyBudgetRequest;
import br.com.abrantes.MoneyControl.dto.response.BudgetSummaryResponse;
import br.com.abrantes.MoneyControl.dto.response.MonthlyBudgetResponse;
import br.com.abrantes.MoneyControl.service.MonthlyBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/budget")
@RestController
@RequiredArgsConstructor
public class MonthlyBudgetController {

    private final MonthlyBudgetService monthlyBudgetService;

    @PostMapping
    public ResponseEntity<MonthlyBudgetResponse> create(
            @Valid @RequestBody CreateMonthlyBudgetRequest request,
            Authentication authentication
    ) {
        MonthlyBudgetResponse response =
                monthlyBudgetService.create(request, authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{budgetId}/summary")
    public ResponseEntity<BudgetSummaryResponse> getSummary(
            @PathVariable Long budgetId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                monthlyBudgetService.getSummary(budgetId, authentication)
        );
    }
}
