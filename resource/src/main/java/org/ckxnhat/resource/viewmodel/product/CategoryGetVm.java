package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.Category;
import org.ckxnhat.resource.service.other.S3Service;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:37.947
 */

public record CategoryGetVm(
        Long id,
        String name,
        String slug,
        String description,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("parent_id") Long parentId
) {
    public static CategoryGetVm fromModel(Category category, S3Service s3Service) {
        return new CategoryGetVm(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                s3Service.getSignedUrl(category.getImageId()),
                category.getParent().getId()
        );
    }
}
