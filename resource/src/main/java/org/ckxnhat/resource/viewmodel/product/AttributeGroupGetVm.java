package org.ckxnhat.resource.viewmodel.product;

import org.ckxnhat.resource.model.product.AttributeGroup;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 08:56:18.439
 */

public record AttributeGroupGetVm(Long id, String name, Long categoryId) {
    public static AttributeGroupGetVm fromModel(AttributeGroup model) {
        return new AttributeGroupGetVm(
                model.getId(),
                model.getName(),
                model.getCategory().getId()
        );
    }
}
