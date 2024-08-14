package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record SpuBriefPutVm(
        String name,
        String slug,
        @JsonProperty("min_price") double minPrice,
        short sort,
        @Min(1) @JsonProperty("brand_id") Long brandId,
        @Min(1) @JsonProperty("category_id") Long categoryId
) {
}
