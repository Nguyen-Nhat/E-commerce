package org.ckxnhat.resource.viewmodel.search;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-31 20:56:45.511
 */

public record SpuListGetVm(List<SpuGetVm> data, int pageNo, int pageSize, long totalElements, int totalPages, boolean isLast) {
}
