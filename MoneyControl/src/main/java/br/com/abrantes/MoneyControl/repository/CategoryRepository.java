package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    @NativeQuery
            (value = """
            SELECT c.id as id,
                   c.name as name
            FROM categorys c
            WHERE c.user_id = :userId
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM categorys c
                    WHERE c.user_id = :userId
"""
            )
    Page<CategorysProjection> getCategorysPage(@Param("userId") Long userId, Pageable pageable);

    Optional<CategoryEntity> findByIdAndUserId(Long id, Long userId);


}
