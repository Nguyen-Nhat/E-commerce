package org.ckxnhat.resource.service.product;

import lombok.RequiredArgsConstructor;
import org.ckxnhat.resource.constants.ErrorCodeConstant;
import org.ckxnhat.resource.exception.DuplicatedException;
import org.ckxnhat.resource.exception.NotFoundException;
import org.ckxnhat.resource.model.product.AttributeGroup;
import org.ckxnhat.resource.model.product.Category;
import org.ckxnhat.resource.repository.product.AttributeGroupRepository;
import org.ckxnhat.resource.repository.product.CategoryRepository;
import org.ckxnhat.resource.viewmodel.product.AttributeGroupGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeGroupListGetVm;
import org.ckxnhat.resource.viewmodel.product.AttributeGroupPostVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 08:53:30.928
 */

@Service
@Transactional
@RequiredArgsConstructor
public class AttributeGroupService {
    private final AttributeGroupRepository attributeGroupRepository;
    private final CategoryRepository categoryRepository;
    public AttributeGroupListGetVm getAttributeGroups(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AttributeGroup> attributeGroups = attributeGroupRepository.findAll(pageable);
        List<AttributeGroupGetVm> data = attributeGroups
                .stream()
                .map(AttributeGroupGetVm::fromModel)
                .toList();
        return new AttributeGroupListGetVm(
                data,
                attributeGroups.getNumber(),
                attributeGroups.getSize(),
                attributeGroups.getTotalElements(),
                attributeGroups.getTotalPages(),
                attributeGroups.isLast()
        );
    }
    public AttributeGroupGetVm getAttributeGroupById(Long id) {
        AttributeGroup attributeGroup = findAttributeGroupById(id);
        return AttributeGroupGetVm.fromModel(attributeGroup);
    }
    public AttributeGroupGetVm createAttributeGroup(AttributeGroupPostVm attributeGroupPostVm) {
        Category category = findCategoryById(attributeGroupPostVm.categoryId());
        if(attributeGroupRepository.existsByName(attributeGroupPostVm.name())) {
            throw new DuplicatedException(ErrorCodeConstant.ATTRIBUTE_GROUP_NAME_IS_DUPLICATED,attributeGroupPostVm.name());
        }
        AttributeGroup attributeGroup = new AttributeGroup();
        attributeGroup.setName(attributeGroupPostVm.name());
        attributeGroup.setCategory(category);
        attributeGroupRepository.save(attributeGroup);
        return AttributeGroupGetVm.fromModel(attributeGroup);
    }
    public AttributeGroupGetVm updateAttributeGroup(Long id, AttributeGroupPostVm attributeGroupPostVm) {
        AttributeGroup attributeGroup = findAttributeGroupById(id);
        Category category = findCategoryById(attributeGroupPostVm.categoryId());
        if(attributeGroupRepository.existsByNameAndIdNot(attributeGroupPostVm.name(), id)) {
            throw new DuplicatedException(ErrorCodeConstant.ATTRIBUTE_GROUP_NAME_IS_DUPLICATED,attributeGroupPostVm.name());
        }
        attributeGroup.setName(attributeGroupPostVm.name());
        attributeGroup.setCategory(category);
        attributeGroupRepository.save(attributeGroup);
        return AttributeGroupGetVm.fromModel(attributeGroup);
    }
    private Category findCategoryById(Long categoryId){
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.CATEGORY_ID_NOT_FOUND, categoryId));
    }
    private AttributeGroup findAttributeGroupById(Long attributeGroupId){
        return attributeGroupRepository
                .findById(attributeGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorCodeConstant.ATTRIBUTE_GROUP_ID_NOT_FOUND, attributeGroupId));
    }
}