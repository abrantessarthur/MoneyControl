package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.InstallmentPlanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

public interface InstallmentPlanRepository extends JpaRepository<InstallmentPlanEntity, Long> {
    @NativeQuery
            (value = """
            SELECT i.id as id,
                   i.description as description,
                   i.total_amount as totalAmount,
                   i.total_installments as totalInstallments,
                   c.id as categoryId
            FROM installment_plans i
            INNER JOIN categorys c ON c.id = i.category_id
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM installment_plans i
"""
            )
    Page<InstallmenPlanProjection> getInstallmentsPage(Pageable pageable);
}
