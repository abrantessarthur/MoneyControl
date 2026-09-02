package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CategoryRequest;
import br.com.abrantes.MoneyControl.dto.response.CategoryResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.TransactionEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.CategorysProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest request, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        CategoryEntity category = CategoryEntity.builder()
                .user(user)
                .name(request.name())
                .build();

        CategoryEntity saved = categoryRepository.save(category);

        return new CategoryResponse(
                saved.getId(),
                saved.getName());
    }

    public void delete(Long id, Authentication authentication){
        CategoryEntity category = findOwnedCategory(id, authentication);
        categoryRepository.deleteById(id);
    }

    public CategoryResponse update(CategoryRequest request, Long id, Authentication authentication){
        CategoryEntity category = findOwnedCategory(id, authentication);
        category.setName(request.name());

        CategoryEntity saved = categoryRepository.save(category);
        return new CategoryResponse(
                saved.getId(),
                saved.getName());
    }

    public Page<CategorysProjection> getCategorysPage(Integer page, Integer size, Authentication authentication){
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return categoryRepository.getCategorysPage(user.getId(), PageRequest.of(page, size));
    }
    private CategoryEntity findOwnedCategory(Long id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
 }
