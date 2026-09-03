package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.MonthlyBudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudgetEntity, Long> {
    Optional<MonthlyBudgetEntity> findByIdAndUserId(Long id, Long userId);
    boolean existsByCategoryId(Long categoryId);
}
