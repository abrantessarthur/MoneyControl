package br.com.abrantes.MoneyControl.service;

import br.com.abrantes.MoneyControl.dto.request.CategoryRequest;
import br.com.abrantes.MoneyControl.dto.response.CategoryResponse;
import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.exception.NotFoundException;
import br.com.abrantes.MoneyControl.repository.CategoryRepository;
import br.com.abrantes.MoneyControl.repository.CategorysProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest request){
        CategoryEntity category = CategoryEntity.builder()
                .name(request.name())
                .build();

        CategoryEntity saved = categoryRepository.save(category);

        return new CategoryResponse(
                saved.getId(),
                saved.getName());
    }

    public void delete(Long id){
        if(!categoryRepository.existsById(id)){
            throw new NotFoundException("Category Not Found");
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponse update(CategoryRequest request, Long id){
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        category.setName(request.name());

        CategoryEntity saved = categoryRepository.save(category);
        return new CategoryResponse(
                saved.getId(),
                saved.getName());
    }

    public Page<CategorysProjection> getCategorysPage(Integer page, Integer size){
        return categoryRepository.getCategorysPage(PageRequest.of(page, size));
    }
 }
