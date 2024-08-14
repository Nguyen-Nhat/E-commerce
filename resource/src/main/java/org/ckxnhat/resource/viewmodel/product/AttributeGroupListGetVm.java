package org.ckxnhat.resource.viewmodel.product;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 09:00:48.311
 */

public record AttributeGroupListGetVm(
        List<AttributeGroupGetVm> attributeGroups,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
