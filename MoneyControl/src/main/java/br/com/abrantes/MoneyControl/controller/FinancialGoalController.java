package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.FinancialGoalRequest;
import br.com.abrantes.MoneyControl.dto.response.FinancialGoalResponse;
import br.com.abrantes.MoneyControl.service.FinancialGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class FinancialGoalController {
    private final FinancialGoalService  financialGoalService;
    @PostMapping
    public ResponseEntity<FinancialGoalResponse> create(@Valid @RequestBody FinancialGoalRequest create,
                                                        Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(financialGoalService.create(authentication, create));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        financialGoalService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<FinancialGoalResponse> update(Authentication authentication,
                                                              @Valid @RequestBody FinancialGoalRequest request,
                                                              @PathVariable Long id){

        return ResponseEntity.ok().body(financialGoalService.update(authentication, request, id));

    }

    @GetMapping
    public List<FinancialGoalResponse> findAll(Authentication authentication) {
        return financialGoalService.findAll(authentication);
    }
}
