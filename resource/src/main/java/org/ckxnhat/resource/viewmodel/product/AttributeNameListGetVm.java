package org.ckxnhat.resource.viewmodel.product;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-04 13:44:06.378
 */

public record AttributeNameListGetVm(
        List<AttributeNameGetVm> attributeNames,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
