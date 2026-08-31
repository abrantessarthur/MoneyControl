package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CreateRecurrencyTransaction;
import br.com.abrantes.MoneyControl.dto.response.RecurrencyTransactionResponse;
import br.com.abrantes.MoneyControl.repository.RecurrencyTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecurrencyTransactionService {
    private final RecurrencyTransactionRepository recurrencyTransactionRepository;

    public RecurrencyTransactionResponse create(Authentication authentication, CreateRecurrencyTransaction create){
        return null;
    }
}
