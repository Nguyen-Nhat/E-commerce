package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.Brand;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:52.125
 */

public record BrandBriefGetVm(
        Long id,
        String name,
        String slug
) {
    public static BrandBriefGetVm fromModel(Brand brand){
        return new BrandBriefGetVm(
                brand.getId(),
                brand.getName(),
                brand.getSlug()
        );
    }
}
