package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.SpuSaleAttributeMapping;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-07 00:25:18.999
 */

public record SpuSaleAttributeMappingGetVm(
        @JsonProperty("attribute_name_id") Long attributeId,
        String name,
        List<SpuSaleAttributeValueGetVm> values
) {
    public static SpuSaleAttributeMappingGetVm fromModel(SpuSaleAttributeMapping spuSaleAttributeMapping){
        return new SpuSaleAttributeMappingGetVm(
                spuSaleAttributeMapping.getAttributeName().getId(),
                spuSaleAttributeMapping.getAttributeName().getName(),
                spuSaleAttributeMapping.getSpuSaleAttributeValues()
                        .stream()
                        .map(SpuSaleAttributeValueGetVm::fromModel)
                        .toList()
        );
    }
    public static List<SpuSaleAttributeMappingGetVm> fromListModel(List<SpuSaleAttributeMapping> spuSaleAttributeMappings){
        return spuSaleAttributeMappings
                .stream()
                .map(SpuSaleAttributeMappingGetVm::fromModel)
                .toList();
    }
}
