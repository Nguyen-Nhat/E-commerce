package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.AttributeName;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 13:43:23.649
 */

public record AttributeNameGetVm(
        Long id,
        String name,
        Long attributeGroupId
) {
    public static AttributeNameGetVm fromModel(AttributeName attributeName) {
        return new AttributeNameGetVm(
                attributeName.getId(),
                attributeName.getName(),
                attributeName.getAttributeGroup().getId()
        );
    }
}
