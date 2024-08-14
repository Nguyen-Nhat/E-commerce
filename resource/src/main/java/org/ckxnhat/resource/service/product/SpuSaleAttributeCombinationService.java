package org.ckxnhat.resource.service.product;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.Spu;
import org.ckxnhat.resource.model.product.SpuSaleAttributeCombination;
import org.ckxnhat.resource.model.product.SpuSaleAttributeValue;
import org.ckxnhat.resource.repository.product.*;
import org.ckxnhat.resource.viewmodel.product.SpuSaleAttributeCombinationGetVm;
import org.ckxnhat.resource.viewmodel.product.SpuSaleAttributeCombinationListGetVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-13 15:33:41.900
 */

@Service
@Transactional
@RequiredArgsConstructor
public class SpuSaleAttributeCombinationService {
    private final SpuSaleAttributeCombinationRepository spuSaleAttributeCombinationRepository;
    private final SpuRepository spuRepository;
    private final SpuSaleAttributeValueRepository spuSaleAttributeValueRepository;
    public SpuSaleAttributeCombinationListGetVm getCombinations(Long spuId, int pageNo, int pageSize) {
        Spu spu = findSpuById(spuId);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SpuSaleAttributeCombination> page = spuSaleAttributeCombinationRepository
                .findAllBySpuId(spuId, pageable);
        List<SpuSaleAttributeCombinationGetVm> data = page
                .stream()
                .map(SpuSaleAttributeCombinationGetVm::fromModel)
                .toList();

        HashMap<Long, SpuSaleAttributeValue> map = transferListAttrValueToHashMap(
                findSaleAttributeValueBySpuId(spuId)
        );

        Gson gson = new Gson();
        Type longListType = new TypeToken<List<Long>>() {}.getType();
        for (SpuSaleAttributeCombinationGetVm combination : data) {
            List<Long> valueIds = gson.fromJson(combination.key(), longListType);
            for(Long valueId : valueIds){
                SpuSaleAttributeValue spuSaleAttributeValue = map.get(valueId);
                combination.attribute().put(
                        spuSaleAttributeValue.getSpuSaleAttributeMapping().getAttributeName().getName(),
                        spuSaleAttributeValue.getValue()
                );
            }
        }
        return new SpuSaleAttributeCombinationListGetVm(
                data,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
    public long generateAllSaleAttributeCombination(Long id){
        Spu spu = findSpuById(id);

        HashSet<String> inUseCombinationKeys = spu.getSpuSaleAttributeCombinations()
                .stream()
                .map(SpuSaleAttributeCombination::getAttributeKey)
                .collect(Collectors.toCollection(HashSet::new));

        List<List<Long>> group = new ArrayList<>();
        for(var item : spu.getSpuSaleAttributeMappings()){
            group.add(item.getSpuSaleAttributeValues()
                    .stream()
                    .map(SpuSaleAttributeValue::getId)
                    .toList()
            );
        }
        List<List<Long>> combination = new ArrayList<>();
        calculateCombination(group, combination,0,new ArrayList<>());
        List<String> allCombinationKeys = combination.stream()
                .map(item -> {
                    Gson gson = new Gson();
                    Collections.sort(item);
                    return gson.toJson(item);
                })
                .toList();

        List<SpuSaleAttributeCombination> data = allCombinationKeys
                .stream()
                .filter(item -> !inUseCombinationKeys.contains(item))
                .map(item -> SpuSaleAttributeCombination.builder()
                        .spu(spu)
                        .sku(null)
                        .attributeKey(item)
                        .build())
                .toList();

         var savedData = spuSaleAttributeCombinationRepository.saveAll(data);
         return savedData.size();
    }
    private Spu findSpuById(Long spuId) {
        return spuRepository.findById(spuId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SPU_ID_NOT_FOUND, spuId));
    }
    private List<SpuSaleAttributeValue> findSaleAttributeValueBySpuId(Long spuId) {
        return spuSaleAttributeValueRepository.findAllBySpuId(spuId);
    }
    private HashMap<Long, SpuSaleAttributeValue> transferListAttrValueToHashMap(
            List<SpuSaleAttributeValue> saleAttributeValues
    ){
        HashMap<Long, SpuSaleAttributeValue> map = new HashMap<>();
        for(var item : saleAttributeValues){
            map.put(item.getId(), item);
        }
        return map;
    }
    private void calculateCombination(List<List<Long>> source, List<List<Long>> result, int depth, List<Long> current){
        if(depth == source.size()){
            result.add(new ArrayList<>(current));
            return;
        }
        for(int i = 0; i < source.get(depth).size(); i++){
            current.add(source.get(depth).get(i));
            calculateCombination(source, result, depth + 1, current);
            current.removeLast();
        }
    }
}
