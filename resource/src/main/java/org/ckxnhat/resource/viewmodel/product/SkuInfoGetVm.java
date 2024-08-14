package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.Sku;
import org.ckxnhat.resource.model.product.SpuSaleAttributeCombination;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:07:47.588
 */

public record SkuInfoGetVm(
        @JsonProperty("id") Long id,
        String gtin,
        double price,
        @JsonProperty("is_deleted") boolean isDeleted,
        String key,
        HashMap<String, String> attribute
) {
    public static SkuInfoGetVm fromModel(Sku sku) {
        return new SkuInfoGetVm(
                sku.getId(),
                sku.getGtin(),
                sku.getPrice(),
                sku.isDeleted(),
                sku.getAttribute().getAttributeKey(),
                sku.getAttribute().getAttribute() != null ? sku.getAttribute().getAttribute(): null
        );
    }
    public static List<SkuInfoGetVm> fromListSpuSaleAttributeCombination(List<SpuSaleAttributeCombination> spuSaleAttributeCombinations) {
        return spuSaleAttributeCombinations
                .stream()
                .map(SpuSaleAttributeCombination::getSku)
                .filter(Objects::nonNull)
                .map(SkuInfoGetVm::fromModel)
                .toList();
    }
}
