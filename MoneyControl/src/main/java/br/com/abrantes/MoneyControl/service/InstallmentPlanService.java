package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.response.InstallmentPlanResponse;
import br.com.abrantes.MoneyControl.entity.InstallmentPlanEntity;
import br.com.abrantes.MoneyControl.repository.InstallmentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstallmentPlanService {
    private final InstallmentPlanRepository installmentPlanRepository;

    public InstallmentPlanResponse create(InstallmentPlanEntity installmentPlanEntity){

    }
}
