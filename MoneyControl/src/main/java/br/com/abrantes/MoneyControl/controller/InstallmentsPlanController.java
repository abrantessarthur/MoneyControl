package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.InstallmentPlanRequest;
import br.com.abrantes.MoneyControl.dto.response.InstallmentPlanResponse;
import br.com.abrantes.MoneyControl.repository.InstallmenPlanProjection;
import br.com.abrantes.MoneyControl.service.InstallmentPlanService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<InstallmentPlanResponse> create(InstallmentPlanRequest installmentPlanRequest, Authentication authentication) {
        installmentPlanService.create(installmentPlanRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        installmentPlanService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<InstallmenPlanProjection> getInstallmentPage(Integer page, Integer size){
        return  installmentPlanService.getInstallmentPage(page, size);
    }
}

