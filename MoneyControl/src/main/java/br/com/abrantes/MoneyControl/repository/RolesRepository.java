package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(String role);
}
