package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.BadRequestException;
import org.ckxnhat.resource.exception.DuplicatedException;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.AttributeName;
import org.ckxnhat.resource.model.product.Spu;
import org.ckxnhat.resource.model.product.SpuRegularAttributeValue;
import org.ckxnhat.resource.repository.product.*;
import org.ckxnhat.resource.viewmodel.product.SpuRegularAttributeGroupGetVm;
import org.ckxnhat.resource.viewmodel.product.SpuRegularAttributePostVm;
import org.h2.api.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-12 13:19:33.841
 */

@Service
@Transactional
@RequiredArgsConstructor
public class SpuRegularAttributeService {
    private final SpuRepository spuRepository;
    private final AttributeNameRepository attributeNameRepository;
    private final SpuRegularAttributeValueRepository spuRegularAttributeValueRepository;
    public void addSpuRegularAttribute(Long spuId, SpuRegularAttributePostVm spuRegularAttributePostVm) {
        Spu spu = findSpuById(spuId);
        AttributeName attributeName = findAttributeNameById(spuRegularAttributePostVm.nameAttributeId());
        checkSpuAndAttributeNameSameCategory(spu,attributeName);

        if(spuRegularAttributeValueRepository.existsBySpuIdAndAttributeNameId(spuId,spuRegularAttributePostVm.nameAttributeId())){
            throw new DuplicatedException(
                    ErrorCodeConstant.ATTRIBUTE_NAME_FOUND_IN_SPU,
                    spuId, attributeName.getId()
            );
        }
        SpuRegularAttributeValue spuRegularAttributeValue = SpuRegularAttributeValue.builder()
                .spu(spu)
                .attributeName(attributeName)
                .value(spuRegularAttributePostVm.valueAttribute())
                .build();
        spuRegularAttributeValueRepository.save(spuRegularAttributeValue);
    }
    public void updateSpuRegularAttribute(Long spuId, SpuRegularAttributePostVm spuRegularAttributePostVm) {
        Spu spu = findSpuById(spuId);
        AttributeName attributeName = findAttributeNameById(spuRegularAttributePostVm.nameAttributeId());
        checkSpuAndAttributeNameSameCategory(spu,attributeName);
        SpuRegularAttributeValue spuRegularAttributeValue = spuRegularAttributeValueRepository
                .findBySpuIdAndAttributeNameId(spuId, spuRegularAttributePostVm.nameAttributeId())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCodeConstant.ATTRIBUTE_NAME_NOT_FOUND_IN_SPU,
                        spuId, attributeName.getId()
                ));
        spuRegularAttributeValue.setValue(spuRegularAttributeValue.getValue());
        spuRegularAttributeValueRepository.save(spuRegularAttributeValue);
    }
    public void deleteSpuRegularAttributes(Long spuId, List<Long> attributeNameIds){
        findSpuById(spuId);
        spuRegularAttributeValueRepository.deleteBySpuIdAndAttributeNameIdIn(spuId, attributeNameIds);
    }
    public List<SpuRegularAttributeGroupGetVm> getSpuRegularAttributes(Long spuId){
        return SpuRegularAttributeGroupGetVm.fromListSpuRegularAttributeValue(
                findSpuSaleAttributeValuesBySpuId(spuId)
        );
    }

    private Spu findSpuById(Long spuId) {
        return spuRepository.findById(spuId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.SPU_ID_NOT_FOUND,spuId));
    }
    private AttributeName findAttributeNameById(Long id) {
        return attributeNameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.ATTRIBUTE_NAME_ID_NOT_FOUND, id));
    }
    private List<SpuRegularAttributeValue> findSpuSaleAttributeValuesBySpuId(Long spuId) {
        return spuRegularAttributeValueRepository.findAllBySpuId(spuId);
    }
    private void checkSpuAndAttributeNameSameCategory(Spu spu, AttributeName attributeName) {
        if(!spu.getCategory().equals(attributeName.getAttributeGroup().getCategory())){
            throw new BadRequestException(
                    ErrorCodeConstant.SPU_AND_ATTRIBUTE_NAME_DIFFERENT_CATEGORY,
                    spu.getId(),
                    attributeName.getId()
            );
        }
    }
}
