package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 15:49:53.520
 */

public record SpuPostVm(
        @NotBlank String name,
        @NotBlank String slug,
        @Min(0) Short sort,
        @JsonProperty("min_price") @Min(0) Double minPrice,
        @JsonProperty("brand_id") @Min(1) Long brandId,
        @JsonProperty("category_id") @Min(1) Long categoryId,
        @JsonProperty("related_spu_ids") List<@Min(1) Long> relatedSpuIds,
        @JsonProperty("sale_attributes") List<SpuSaleAttributePostVm> saleAttributes,
        @JsonProperty("regular_attributes") List<SpuRegularAttributePostVm> regularAttributes
) {
}
