package org.ckxnhat.resource.viewmodel.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-08 21:04:22.048
 */

public record CategoryPostVm(
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        Long parentId,
        MultipartFile image
) {
}
