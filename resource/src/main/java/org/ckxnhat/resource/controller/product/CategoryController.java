package org.ckxnhat.resource.controller.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.PageConstant;
import org.ckxnhat.resource.service.product.CategoryService;
import org.ckxnhat.resource.viewmodel.product.CategoryGetVm;
import org.ckxnhat.resource.viewmodel.product.CategoryListGetVm;
import org.ckxnhat.resource.viewmodel.product.CategoryPostVm;
import org.ckxnhat.resource.viewmodel.response.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-03 12:53:25.702
 */

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getCategories (
            @RequestParam(name = PageConstant.PAGE_NUMBER_NAME, defaultValue = PageConstant.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = PageConstant.PAGE_SIZE_NAME, defaultValue = PageConstant.PAGE_SIZE, required = false) int pageSize
    ){
        CategoryListGetVm data = categoryService.getCategories(pageNo, pageSize);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getCategoryById (@Min(1) @PathVariable Long id) {
        CategoryGetVm data = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @GetMapping("/slug/{slug}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getCategoryById (@NotBlank @PathVariable String slug) {
        CategoryGetVm data = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@Valid @ModelAttribute CategoryPostVm categoryPostVm, UriComponentsBuilder uriBuilder){
        CategoryGetVm data = categoryService.createCategory(categoryPostVm);
        return ResponseEntity.created(
                        uriBuilder.replacePath("/category/{id}").buildAndExpand(data.id()).toUri()
                )
                .body(new SuccessApiResponse(
                        HttpStatus.CREATED.getReasonPhrase(),
                        data
                ));
    }
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(
            @Min(1) Long id,
            @Valid @ModelAttribute CategoryPostVm categoryPostVm
    ){
        CategoryGetVm data = categoryService.updateCategory(id, categoryPostVm);
        return ResponseEntity.ok(new SuccessApiResponse(
                HttpStatus.OK.getReasonPhrase(),
                data
        ));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@Min(1) @PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
