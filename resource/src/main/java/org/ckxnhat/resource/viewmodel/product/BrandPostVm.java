package org.ckxnhat.resource.viewmodel.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-02 10:02:36.106
 */

public record BrandPostVm(@NotBlank String name,@NotBlank String slug, String description,MultipartFile image) {
}
