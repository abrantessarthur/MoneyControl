package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CreateFinancialGoal;
import br.com.abrantes.MoneyControl.dto.request.UpdateFinancialGoalRequest;
import br.com.abrantes.MoneyControl.dto.response.FinancialGoalResponse;
import br.com.abrantes.MoneyControl.dto.response.UpdateFinancialGoalResponse;
import br.com.abrantes.MoneyControl.entity.UserEntity;
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
    public ResponseEntity<FinancialGoalResponse> create(@Valid @RequestBody CreateFinancialGoal create,
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
    public ResponseEntity<UpdateFinancialGoalResponse> update(Authentication authentication,
                                                              @Valid @RequestBody UpdateFinancialGoalRequest request,
                                                              @PathVariable Long id){
        financialGoalService.update(authentication, request, id);
        return ResponseEntity.ok().build();

    }

    @GetMapping
    public List<FinancialGoalResponse> getAll(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return financialGoalService.findAll(authentication);
    }
}
