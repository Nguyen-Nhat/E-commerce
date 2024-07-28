package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SkuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:08:11.106
 */

@Repository
public interface SkuImageRepository extends JpaRepository<SkuImage, Long> {
}
