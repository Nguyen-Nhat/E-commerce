package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 09:01:41.267
 */

public record AttributeGroupPostVm(
        @NotBlank String name,
        @JsonProperty("category_id") @Min(1) Long categoryId
) {
}
