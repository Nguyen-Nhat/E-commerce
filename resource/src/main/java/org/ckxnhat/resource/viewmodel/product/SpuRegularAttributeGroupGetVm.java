package org.ckxnhat.resource.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ckxnhat.resource.model.product.SpuRegularAttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-11 00:25:46.080
 */

public record SpuRegularAttributeGroupGetVm(
        @JsonProperty("group_name") String groupName,
        List<SpuRegularAttributeGetVm> attribute
) {
    public static List<SpuRegularAttributeGroupGetVm> fromListSpuRegularAttributeValue(
        List<SpuRegularAttributeValue> spuRegularAttributeValue
    ){
        Map<String, List<SpuRegularAttributeValue>> regularAttributeMap = new HashMap<>();
        for(SpuRegularAttributeValue attr : spuRegularAttributeValue){
            String key = attr.getAttributeName().getAttributeGroup().getName();
            List<SpuRegularAttributeValue> value = regularAttributeMap.get(key);
            if(value == null){
                value = new ArrayList<>();
            }
            value.add(attr);
            regularAttributeMap.put(key, value);
        }
        List<SpuRegularAttributeGroupGetVm> regularAttribute = new ArrayList<>();
        for(var entry : regularAttributeMap.entrySet()){
            String key = entry.getKey();
            List<SpuRegularAttributeValue> value = entry.getValue();
            regularAttribute.add(new SpuRegularAttributeGroupGetVm(
                    key,
                    value.stream()
                            .map(SpuRegularAttributeGetVm::fromModel)
                            .toList()
            ));
        }
        return regularAttribute;
    }
}
