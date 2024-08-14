package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.Category;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:42.918
 */

public record CategoryBriefGetVm(
        Long id,
        String name,
        String slug
) {
    public static CategoryBriefGetVm fromModel(Category category){
        return new CategoryBriefGetVm(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }
}
