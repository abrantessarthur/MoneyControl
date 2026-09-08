package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CategoryRequest;
import br.com.abrantes.MoneyControl.dto.response.CategoryResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.entity.UserEntity;
import br.com.abrantes.MoneyControl.exception.BadRequestException;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final RecurrencyTransactionRepository recurrencyTransactionRepository;
    private final MonthlyBudgetRepository  monthlyBudgetRepository;
    private final InstallmentPlanRepository  installmentPlanRepository;

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

    @Transactional
    public void delete(Long id, Authentication authentication){
        CategoryEntity category = findOwnedCategory(id, authentication);
        if (transactionRepository.existsByCategoryId(id) || recurrencyTransactionRepository.existsByCategoryId(id)
                || monthlyBudgetRepository.existsByCategoryId(id) || installmentPlanRepository.existsByCategoryId(id)) {
            throw new BadRequestException("Category is linked to another resource");
        }
        categoryRepository.delete(category);
    }

    public CategoryResponse update(CategoryRequest request, Long id, Authentication authentication){
        CategoryEntity category = findOwnedCategory(id, authentication);
        category.setName(request.name());

        CategoryEntity saved = categoryRepository.save(category);
        return new CategoryResponse(
                saved.getId(),
                saved.getName());
    }

    public Page<CategoriesProjection> getCategoriesPage(Integer page, Integer size, Authentication authentication){
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return categoryRepository.getCategoriesPage(user.getId(), PageRequest.of(page, size));
    }

    private CategoryEntity findOwnedCategory(Long id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
 }
