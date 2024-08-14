package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.SpuSaleAttributeValue;

public record SpuSaleAttributeValueGetVm(
        Long id,
        String value
) {
    public static SpuSaleAttributeValueGetVm fromModel(SpuSaleAttributeValue spuSaleAttributeValue){
        return new SpuSaleAttributeValueGetVm(
                spuSaleAttributeValue.getId(),
                spuSaleAttributeValue.getValue()
        );
    }
}
