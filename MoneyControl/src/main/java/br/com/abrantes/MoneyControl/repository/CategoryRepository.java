package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    @NativeQuery(
            value = """
                    SELECT c.id AS id, c.name AS name
                    FROM categories c
                    WHERE c.user_id = :userId
                    ORDER BY c.name
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM categories c
                    WHERE c.user_id = :userId
                    """
    )
    Page<CategoriesProjection> getCategoriesPage(@Param("userId") Long userId, Pageable pageable);

    Optional<CategoryEntity> findByIdAndUserId(Long id, Long userId);


}
