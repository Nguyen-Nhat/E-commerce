package org.ckxnhat.resource.viewmodel.product;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-13 15:45:02.440
 */

public record SpuSaleAttributeCombinationListGetVm(
        List<SpuSaleAttributeCombinationGetVm> data,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
