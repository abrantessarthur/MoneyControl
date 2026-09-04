package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.CreditCardEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {
    Optional<CreditCardEntity> findByIdAndUserId(Long id, Long userId);

}
