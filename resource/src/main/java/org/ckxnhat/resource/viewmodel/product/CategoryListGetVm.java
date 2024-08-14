package org.ckxnhat.resource.viewmodel.product;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:33.897
 */

public record CategoryListGetVm(List<CategoryGetVm> categories, int pageNo, int pageSize, long totalElements, int totalPages, boolean isLast) {
}
