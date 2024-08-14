package org.ckxnhat.resource.viewmodel.product;

import java.util.List;

public record SpuListGetVm(List<SpuGetVm> spus, int pageNo, int pageSize, long totalElements, int totalPages, boolean isLast) {
}
