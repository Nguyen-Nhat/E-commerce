package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 13:44:10.795
 */

public record AttributeNamePostVm(
        @NotBlank String name,
        @Min(1) @JsonProperty("attribute_group_id") Long attributeGroupId
) {
}
