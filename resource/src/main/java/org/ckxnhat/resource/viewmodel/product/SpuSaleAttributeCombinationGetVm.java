package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.SpuSaleAttributeCombination;

import java.util.HashMap;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-13 15:44:59.024
 */

public record SpuSaleAttributeCombinationGetVm(
        Long id,
        @JsonProperty("spu_id") Long spuId,
        @JsonProperty("sku_id") Long skuId,
        String key,
        HashMap<String, String> attribute
) {
    public static SpuSaleAttributeCombinationGetVm fromModel(SpuSaleAttributeCombination model) {
        return new SpuSaleAttributeCombinationGetVm(
                model.getId(),
                model.getSpu().getId(),
                model.getSku() != null ? model.getSku().getId() : null,
                model.getAttributeKey(),
                model.getAttribute()
        );
    }
}
