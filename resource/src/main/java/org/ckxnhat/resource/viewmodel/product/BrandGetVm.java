package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.Brand;
import org.ckxnhat.resource.service.other.S3Service;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-01 21:57:38.479
 */

public record BrandGetVm(Long id, String name, String slug, String description, String imageUrl) {
    public static BrandGetVm fromModel(Brand brand, S3Service s3Service){
        return new BrandGetVm(
                brand.getId(),
                brand.getName(),
                brand.getSlug(),
                brand.getDescription(),
                s3Service.getSignedUrl(brand.getImageId())
        );
    }
}
