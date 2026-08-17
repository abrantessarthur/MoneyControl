package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByToken(String token);

    @NativeQuery
            (value = """
            SELECT u.id as id,
                   u.name as name,
                   u.email as email
            FROM usuarios u
            
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM users u
"""
            )
    Page<UsersProjection> getUsersPage(Pageable pageable);
}
