package org.ckxnhat.resource.viewmodel.search;

import org.ckxnhat.resource.document.Spu;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-31 20:24:19.708
 */

public record SpuGetVm(String name, String thumbnailUrl, String slug, Double price, Long brandId, Long categoryId) {
    public static SpuGetVm fromModel(Spu spu){
        return new SpuGetVm(spu.getName(), spu.getThumbnailId(), spu.getSlug(), spu.getPrice(), spu.getBrandId(), spu.getCategoryId());
    }
}
