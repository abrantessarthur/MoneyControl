package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.InstallmentPlanRequest;
import br.com.abrantes.MoneyControl.dto.response.InstallmentPlanResponse;
import br.com.abrantes.MoneyControl.repository.InstallmenPlanProjection;
import br.com.abrantes.MoneyControl.service.InstallmentPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/installments")
public class InstallmentsPlanController {
    private final InstallmentPlanService installmentPlanService;

    @PostMapping
    public ResponseEntity<InstallmentPlanResponse> create(@Valid @RequestBody InstallmentPlanRequest installmentPlanRequest,
                                                          Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(installmentPlanService.create(installmentPlanRequest, authentication));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication){
        installmentPlanService.delete(id, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<InstallmenPlanProjection> getInstallmentPage(@PathVariable Integer page, @PathVariable Integer size,
                                                             Authentication authentication){
        return installmentPlanService.getInstallmentPage(page, size, authentication);
    }
}

