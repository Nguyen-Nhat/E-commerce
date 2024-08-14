package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-06 00:01:11.178
 */

public record SpuRegularAttributePostVm(
        @JsonProperty("name_attribute_id") @Min(1) Long nameAttributeId,
        @JsonProperty("value_attribute") @NotBlank String valueAttribute
) {
}
