package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.InstallmentPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentPlanRepository extends JpaRepository<InstallmentPlanEntity, Long> {
}
