package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record SpuIAllImagePostVm(
        @JsonProperty("thumbnail_image") MultipartFile thumbnailImage,
        @JsonProperty("description_images") List<MultipartFile> descriptionImages,
        @JsonProperty("images") List<MultipartFile> images
) {
}
