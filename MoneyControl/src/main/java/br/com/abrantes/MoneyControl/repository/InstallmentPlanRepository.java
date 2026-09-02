package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.InstallmentPlanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InstallmentPlanRepository extends JpaRepository<InstallmentPlanEntity, Long> {
    Optional<InstallmentPlanEntity> findByIdAndUserId(Long id, Long userId);

    @NativeQuery
            (value = """
            SELECT i.id as id,
                   i.description as description,
                   i.total_amount as totalAmount,
                   i.total_installments as totalInstallments,
                   c.id as categoryId
            FROM installment_plans i
            INNER JOIN categorys c ON c.id = i.category_id
            WHERE i.user_id = :userId
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM installment_plans i
                    WHERE i.user_id = :userId
"""
            )
    Page<InstallmenPlanProjection> getInstallmentsPage(@Param("userId") Long userId, Pageable pageable);
}
