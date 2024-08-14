package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.Spu;
import org.ckxnhat.resource.service.other.S3Service;

import java.util.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:12.115
 */

public record SpuDetailGetVm(
        Long id,
        String name,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        String slug,
        @JsonProperty("min_price") Double minPrice,
        BrandBriefGetVm brand,
        CategoryBriefGetVm category,
        @JsonProperty("images") List<SpuImageGetVm> spuImages,
        @JsonProperty("related_spus") List<SpuGetVm> relatedSpus,
        @JsonProperty("spu_descriptions") List<SpuDescriptionGetVm> spuDescriptions,
        @JsonProperty("regular_attribute") List<SpuRegularAttributeGroupGetVm> regularAttribute,
        @JsonProperty("sale_attribute") List<SpuSaleAttributeMappingGetVm> saleAttribute,
        List<SkuInfoGetVm> skus
) {
    public static SpuDetailGetVm fromModel(Spu spu, S3Service s3Service){
        return new SpuDetailGetVm(
                spu.getId(),
                spu.getName(),
                s3Service.getSignedUrl(spu.getThumbnailId()),
                spu.getSlug(),
                spu.getMinPrice(),
                BrandBriefGetVm.fromModel(spu.getBrand()),
                CategoryBriefGetVm.fromModel(spu.getCategory()),
                SpuImageGetVm.fromListModel(spu.getImages(), s3Service),
                SpuGetVm.fromListRelatedSpu(spu.getRelatedSpus(), s3Service),
                SpuDescriptionGetVm.fromListModel(spu.getDescriptions(), s3Service),
                SpuRegularAttributeGroupGetVm.fromListSpuRegularAttributeValue(spu.getRegularAttributeValues()),
                SpuSaleAttributeMappingGetVm.fromListModel(spu.getSpuSaleAttributeMappings()),
                SkuInfoGetVm.fromListSpuSaleAttributeCombination(spu.getSpuSaleAttributeCombinations())
        );
    }
}
