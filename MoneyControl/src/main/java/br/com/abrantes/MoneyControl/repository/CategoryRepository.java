package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    @NativeQuery
            (value = """
            SELECT c.id as id,
                   c.name as name,
            FROM categorys c
            
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM categorys c
"""
            )
    Page<CategorysProjection> getCategorysPage(Pageable pageable);
}
