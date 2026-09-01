package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateFinancialGoal;
import br.com.abrantes.MoneyControl.dto.response.FinancialGoalResponse;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.repository.FinancialGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;

    public FinancialGoalResponse create(Authentication authentication, CreateFinancialGoal create) {
        UserEntity user = (UserEntity) authentication.getPrincipal();


    }
}
