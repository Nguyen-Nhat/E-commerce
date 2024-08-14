package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.Spu;
import org.ckxnhat.resource.model.product.SpuRelated;
import org.ckxnhat.resource.service.other.S3Service;

import java.util.List;

public record SpuGetVm(
        Long id,
        String name,
        String slug,
        String thumbnailUrl,
        double minPrice,
        BrandBriefGetVm brand,
        CategoryBriefGetVm category
) {
    public static SpuGetVm fromModel(Spu spu, S3Service s3Service){
        return new SpuGetVm(
                spu.getId(),
                spu.getName(),
                spu.getSlug(),
                s3Service.getSignedUrl(spu.getThumbnailId()),
                spu.getMinPrice(),
                BrandBriefGetVm.fromModel(spu.getBrand()),
                CategoryBriefGetVm.fromModel(spu.getCategory())
        );
    }
    public static List<SpuGetVm> fromListRelatedSpu(List<SpuRelated> spuList, S3Service s3Service){
        return spuList
                .stream()
                .map(SpuRelated::getRelatedSpu)
                .map(item -> SpuGetVm.fromModel(item, s3Service))
                .toList();
    }
}
