package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.SpuRegularAttributeValue;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:06:04.120
 */

public record SpuRegularAttributeGetVm(
        @JsonProperty("attribute_name_id") Long attributeId,
        String name,
        String value
) {
    public static SpuRegularAttributeGetVm fromModel(SpuRegularAttributeValue spuRegularAttributeValue){
        return new SpuRegularAttributeGetVm(
                spuRegularAttributeValue.getAttributeName().getId(),
                spuRegularAttributeValue.getAttributeName().getName(),
                spuRegularAttributeValue.getValue()
        );
    }
}
