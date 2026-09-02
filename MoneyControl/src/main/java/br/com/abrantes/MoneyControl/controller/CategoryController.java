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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categorys")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                           Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(categoryRequest, authentication));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Authentication authentication) {
        categoryService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @Valid @RequestBody CategoryRequest categoryRequest,
                                                           Authentication authentication){

        return ResponseEntity.ok()
                .body(categoryService.update(categoryRequest, id, authentication));
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<CategorysProjection> getCategorysPage(@PathVariable Integer page,
                                                      @PathVariable Integer size,
                                                      Authentication authentication){
        return categoryService.getCategorysPage(page, size, authentication);
    }
}
