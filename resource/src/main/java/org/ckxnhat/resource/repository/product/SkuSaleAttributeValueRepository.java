package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SkuSaleAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:10:31.839
 */

@Repository
public interface SkuSaleAttributeValueRepository extends JpaRepository<SkuSaleAttributeValue, Long> {
}
