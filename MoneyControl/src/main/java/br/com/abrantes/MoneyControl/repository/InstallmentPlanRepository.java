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
                   i.amount as amount,
                   i.type_transactional as typeTransactional,
                   c.id as categoryId,
                   c.name as categoryName,
                   i.date as date
            FROM installments t
            INNER JOIN categorys c ON c.id = t.category_id
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM installments_plans i
"""
            )
    Page<InstallmenPlanProjection> getInstallmentsPage(Pageable pageable);
}
