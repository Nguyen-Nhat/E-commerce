package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.AttributeGroup;
import org.ckxnhat.resource.model.product.AttributeName;
import org.ckxnhat.resource.repository.product.AttributeGroupRepository;
import org.ckxnhat.resource.repository.product.AttributeNameRepository;
import org.ckxnhat.resource.viewmodel.product.AttributeNameGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeNameListGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeNamePostVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 13:40:31.164
 */

@Service
@RequiredArgsConstructor
@Transactional
public class AttributeNameService {
    private final AttributeNameRepository attributeNameRepository;
    private final AttributeGroupRepository attributeGroupRepository;
    public AttributeNameListGetVm getAttributeNames(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AttributeName> attributeNames = attributeNameRepository.findAll(pageable);
        List<AttributeNameGetVm> data = attributeNames
                .stream()
                .map(AttributeNameGetVm::fromModel)
                .toList();
        return new AttributeNameListGetVm(
                data,
                attributeNames.getNumber(),
                attributeNames.getSize(),
                attributeNames.getTotalElements(),
                attributeNames.getTotalPages(),
                attributeNames.isLast()
        );
    }
    public AttributeNameGetVm getAttributeNameById(Long id) {
        AttributeName attributeName = findAttributeNameById(id);
        return AttributeNameGetVm.fromModel(attributeName);
    }
    public AttributeNameGetVm createAttributeName(AttributeNamePostVm attributeNamePostVm) {
        AttributeGroup attributeGroup = findAttributeGroupById(attributeNamePostVm.attributeGroupId());
        AttributeName attributeName = new AttributeName();
        attributeName.setName(attributeNamePostVm.name());
        attributeName.setAttributeGroup(attributeGroup);
        attributeNameRepository.save(attributeName);
        return AttributeNameGetVm.fromModel(attributeName);
    }
    public AttributeNameGetVm updateAttributeName(Long id, AttributeNamePostVm attributeNamePostVm) {
        AttributeName attributeName = findAttributeNameById(id);
        AttributeGroup attributeGroup = findAttributeGroupById(attributeNamePostVm.attributeGroupId());
        attributeName.setName(attributeNamePostVm.name());
        attributeName.setAttributeGroup(attributeGroup);
        attributeNameRepository.save(attributeName);
        return AttributeNameGetVm.fromModel(attributeName);
    }
    private AttributeGroup findAttributeGroupById(Long attributeGroupId){
        return attributeGroupRepository
                .findById(attributeGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.ATTRIBUTE_GROUP_ID_NOT_FOUND, attributeGroupId));
    }
    private AttributeName findAttributeNameById(Long attributeNameId){
        return attributeNameRepository
                .findById(attributeNameId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.ATTRIBUTE_NAME_ID_NOT_FOUND, attributeNameId));
    }
}
