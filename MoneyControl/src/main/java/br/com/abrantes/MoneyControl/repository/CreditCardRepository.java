package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {
}
