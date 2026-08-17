package br.com.abrantes.MoneyControl.controller;

import br.com.abrantes.MoneyControl.dto.request.CategoryRequest;
import br.com.abrantes.MoneyControl.dto.response.CategoryResponse;
import br.com.abrantes.MoneyControl.repository.CategorysProjection;
import br.com.abrantes.MoneyControl.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categorys")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        categoryService.create(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @RequestBody CategoryRequest categoryRequest){
        categoryService.update(categoryRequest, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<CategorysProjection> getCategorysPage(@PathVariable Integer page, @PathVariable Integer size){
        return categoryService.getCategorysPage(page, size);
    }
}
