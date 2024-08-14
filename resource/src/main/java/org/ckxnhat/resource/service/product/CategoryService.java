package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.BadRequestException;
import org.ckxnhat.resource.exception.DuplicatedException;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.Category;
import org.ckxnhat.resource.repository.product.CategoryRepository;
import org.ckxnhat.resource.service.other.S3Service;
import org.ckxnhat.resource.viewmodel.product.CategoryGetVm;
import org.ckxnhat.resource.viewmodel.product.CategoryListGetVm;
import org.ckxnhat.resource.viewmodel.product.CategoryPostVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-03 12:49:25.075
 */

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    public CategoryListGetVm getCategories(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryGetVm> data = categories
                .stream()
                .map(item -> CategoryGetVm.fromModel(item, s3Service))
                .toList();
        return new CategoryListGetVm(
                data,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isLast()
        );
    }
    public CategoryGetVm getCategoryById(Long id) {
        Category category = findCategoryById(id);
        checkCategoryIsDeleted(category);
        return CategoryGetVm.fromModel(category,s3Service);
    }
    public CategoryGetVm getCategoryBySlug(String slug) {
        Category category = findCategoryBySlug(slug);
        checkCategoryIsDeleted(category);
        return CategoryGetVm.fromModel(category,s3Service);
    }
    public CategoryGetVm createCategory(CategoryPostVm categoryPostVm) {
        if(categoryRepository.existsBySlug(categoryPostVm.slug())){
            throw new DuplicatedException(ErrorCodeConstant.CATEGORY_SLUG_IS_DUPLICATED, categoryPostVm.slug());
        }
        Category parent = null;
        if(categoryPostVm.parentId() != null){
            parent = findCategoryById(categoryPostVm.parentId());
        }
        String imageId = s3Service.uploadImage(categoryPostVm.image());
        Category category = Category
                .builder()
                .name(categoryPostVm.name())
                .slug(categoryPostVm.slug())
                .description(categoryPostVm.description())
                .imageId(imageId)
                .parent(parent)
                .build();
        categoryRepository.save(category);
        return CategoryGetVm.fromModel(category,s3Service);
    }
    public CategoryGetVm updateCategory(Long id, CategoryPostVm categoryPostVm) {
        Category category = findCategoryById(id);
        if(!categoryPostVm.slug().equals(category.getSlug())
                && categoryRepository.existsBySlug(categoryPostVm.slug())
        ){
            throw new DuplicatedException(ErrorCodeConstant.CATEGORY_SLUG_IS_DUPLICATED, categoryPostVm.slug());
        }
        s3Service.deleteImage(category.getImageId());
        String newImageId = s3Service.uploadImage(categoryPostVm.image());
        category.setImageId(newImageId);
        category.setName(categoryPostVm.name());
        category.setSlug(categoryPostVm.slug());
        category.setDescription(categoryPostVm.description());
        if(categoryPostVm.parentId() != null){
            Category parent = findCategoryById(categoryPostVm.parentId());
            if(checkParent(id, parent)){
                throw new BadRequestException(ErrorCodeConstant.PARENT_CATEGORY_CANNOT_BE_ITSELF, id);
            }
            category.setParent(parent);
        }
        else {
            category.setParent(null);
        }
        categoryRepository.save(category);
        return CategoryGetVm.fromModel(category,s3Service);
    }

    public void deleteCategoryById(Long id) {
        Category category = findCategoryById(id);
        category.setDeleted(true);
        categoryRepository.save(category);
    }
    private Category findCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.CATEGORY_ID_NOT_FOUND, id));
    }
    private Category findCategoryBySlug(String slug){
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.CATEGORY_SLUG_NOT_FOUND, slug));
    }
    private void checkCategoryIsDeleted(Category category){
        if(category.isDeleted()){
            throw new BadRequestException(ErrorCodeConstant.CATEGORY_IS_DELETED, category.getId());
        }
    }
    private boolean checkParent(Long parentId, Category category){
        if(parentId.equals(category.getId())) return true;
        if(category.getParent() != null)
            return checkParent(parentId, category.getParent());
        return false;
    }
}
