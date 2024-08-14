package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.SpuImage;
import org.ckxnhat.resource.service.other.S3Service;

import java.util.List;

public record SpuImageGetVm(
        Long id,
        @JsonProperty("image_url") String imageUrl
) {
    public static SpuImageGetVm fromModel(SpuImage spuImage, S3Service s3Service) {
        return new SpuImageGetVm(
                spuImage.getId(),
                s3Service.getSignedUrl(spuImage.getImageId())
        );
    }
    public static List<SpuImageGetVm> fromListModel(List<SpuImage> spuImages, S3Service s3Service) {
        return spuImages
                .stream()
                .map(item -> SpuImageGetVm.fromModel(item, s3Service))
                .toList();
    }
}
