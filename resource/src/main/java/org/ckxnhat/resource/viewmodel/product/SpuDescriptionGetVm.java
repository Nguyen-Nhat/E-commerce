package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.SpuDescription;
import org.ckxnhat.resource.service.other.S3Service;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:17.888
 */

public record SpuDescriptionGetVm(
        Long id,
        @JsonProperty("image_url") String imageUrl
) {
    public static SpuDescriptionGetVm fromModel(SpuDescription spuDescription, S3Service s3Service){
        return new SpuDescriptionGetVm(
                spuDescription.getId(),
                s3Service.getSignedUrl(spuDescription.getImageId())
        );
    }
    public static List<SpuDescriptionGetVm> fromListModel(List<SpuDescription> spuDescriptionList, S3Service s3Service){
        return spuDescriptionList
                .stream()
                .map(item -> SpuDescriptionGetVm.fromModel(item, s3Service))
                .toList();
    }
}
