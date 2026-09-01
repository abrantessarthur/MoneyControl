package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.RecurringTransactionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecurrencyTransactionRepository extends JpaRepository<RecurringTransactionEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT recurring
            FROM RecurringTransactionEntity recurring
            WHERE recurring.active = true
              AND recurring.nextExecutionDate <= :referenceDate
            ORDER BY recurring.nextExecutionDate
            """)
    List<RecurringTransactionEntity> findDueForUpdate(
            @Param("referenceDate") LocalDate referenceDate
    );

    List<RecurringTransactionEntity> findAllByUserIdOrderByNextExecutionDateAsc(Long userId);

    Optional<RecurringTransactionEntity> findByIdAndUserId(Long id, Long userId);
}
