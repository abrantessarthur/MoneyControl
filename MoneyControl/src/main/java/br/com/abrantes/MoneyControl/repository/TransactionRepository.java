package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.dto.response.TransactionResponse;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @NativeQuery
            (value = """
            SELECT t.id as id,
                   t.description as description,
                   t.amount as amount,
                   t.typeTransactional as type_transactional,
                   t.category as category,
                   t.date as date
            FROM transactions t
            
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM transactions t
"""
            )
    Page<TransactionsProjection> getTransactionsPage(Pageable pageable);

    @Query("""
    SELECT t.transaction 
    FROM TransactionEntity t
    GROUP BY t.transaction
    ORDER BY SUM(t.amount) DESC
""")
    List<TransactionResponse> findMostExpensiveTransaction(Pageable pageable);
}
