package br.com.abrantes.MoneyControl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class RecurringTransactionScheduler {

    private static final ZoneId APPLICATION_ZONE = ZoneId.of("America/Sao_Paulo");

    private final RecurrencyTransactionService recurrencyTransactionService;

    @Scheduled(
            cron = "${app.recurring-transactions.cron:0 0 2 * * *}",
            zone = "${app.time-zone:America/Sao_Paulo}"
    )
    public void generateDueTransactions() {
        recurrencyTransactionService.processDueTransactions(
                LocalDate.now(APPLICATION_ZONE)
        );
    }
}
