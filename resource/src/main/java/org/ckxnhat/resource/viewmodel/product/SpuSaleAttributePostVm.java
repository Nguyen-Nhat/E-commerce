package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 23:42:40.337
 */

public record SpuSaleAttributePostVm(
        @JsonProperty("name_attribute_id") @Min(1) Long nameAttributeId,
        @JsonProperty("value_attributes") List<@NotBlank String> valueAttributes
) {
}
