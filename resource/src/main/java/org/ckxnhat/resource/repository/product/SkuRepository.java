package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:07:10.250
 */

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {
}
