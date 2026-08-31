package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.RecurringTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurrencyTransactionRepository extends JpaRepository<RecurringTransactionEntity, Long> {
}
