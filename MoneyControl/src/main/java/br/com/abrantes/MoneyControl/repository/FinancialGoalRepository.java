package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.FinancialGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoalEntity, Long> {
}
