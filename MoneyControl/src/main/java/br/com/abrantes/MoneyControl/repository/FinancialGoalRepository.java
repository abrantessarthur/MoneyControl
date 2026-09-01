package br.com.abrantes.MoneyControl.repository;
import br.com.abrantes.MoneyControl.entity.FinancialGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoalEntity, Long> {
    Optional<FinancialGoalEntity> findByIdAndUserId(Long id, Long userId);
    List<FinancialGoalEntity> findAllByUserId(Long userId);

}
