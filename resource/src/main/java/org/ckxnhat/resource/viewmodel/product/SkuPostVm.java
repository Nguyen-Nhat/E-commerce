package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-13 18:26:50.545
 */

public record SkuPostVm(
        @Min(1) @JsonProperty("spu_sale_attribute_combination_id") Long spuSaleAttributeCombinationId,
        @NotBlank String gtin,
        @Min(0) double price
) {
}
