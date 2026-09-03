package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.dto.response.TransactionResponse;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findByIdAndUserId(Long id, Long userId);

    boolean existsByRecurringTransactionIdAndRecurrenceReferenceDate(
            Long recurringTransactionId,
            LocalDate recurrenceReferenceDate
    );

    @NativeQuery
            (value = """
            SELECT t.id as id,
                   t.description as description,
                   t.amount as amount,
                   t.type_transactional as typeTransactional,
                   c.id as categoryId,
                   c.name as categoryName,
                   t.date as date
            FROM transactions t
            INNER JOIN categorys c ON c.id = t.category_id
            WHERE t.user_id = :userId
            ORDER BY t.date DESC
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM transactions t
                    WHERE t.user_id = :userId
"""
            )
    Page<TransactionsProjection> getTransactionsPage(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT new br.com.abrantes.MoneyControl.dto.response.TransactionResponse(
        t.id,
        t.description,
        t.amount,
        t.typeTransactional,
        t.category.id,
        t.category.name,
        t.date
    )
    FROM TransactionEntity t
    WHERE t.user.id = :userId
    ORDER BY t.amount DESC
""")
    List<TransactionResponse> findMostExpensiveTransaction(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT COALESCE(
        SUM(
            CASE
                WHEN t.typeTransactional = br.com.abrantes.MoneyControl.enums.TypeTransactional.INCOME
                    THEN t.amount
                ELSE -t.amount
            END
        ),
        0
    )
    FROM TransactionEntity t
    WHERE t.user.id = :userId
""")
    BigDecimal calculateBalance(@Param("userId") Long userId);

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM TransactionEntity t
    WHERE t.user.id = :userId
      AND t.category.id = :categoryId
      AND t.typeTransactional = br.com.abrantes.MoneyControl.enums.TypeTransactional.EXPENSE
      AND t.date >= :startDate
      AND t.date < :endDate
""")
    BigDecimal calculateSpent(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        COALESCE(SUM(CASE WHEN t.typeTransactional = br.com.abrantes.MoneyControl.enums.TypeTransactional.INCOME THEN t.amount ELSE 0 END), 0) AS totalIncome,
        COALESCE(SUM(CASE WHEN t.typeTransactional = br.com.abrantes.MoneyControl.enums.TypeTransactional.EXPENSE THEN t.amount ELSE 0 END), 0) AS totalExpense
    FROM TransactionEntity t
    WHERE t.user.id = :userId
""")
    TransactionSummary getSumary(@Param("userId") Long userId);

    boolean existsByCategoryId(Long categoryId);
}
