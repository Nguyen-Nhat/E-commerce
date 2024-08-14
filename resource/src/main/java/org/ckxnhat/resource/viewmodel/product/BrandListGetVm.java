package org.ckxnhat.resource.viewmodel.product;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-01 22:03:31.355
 */

public record BrandListGetVm(List<BrandGetVm> brands, int pageNo, int pageSize, long totalElements, int totalPages, boolean isLast) {
}
