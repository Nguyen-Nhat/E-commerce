package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuRegularAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:27:35.606
 */

@Repository
public interface SpuRegularAttributeValueRepository extends JpaRepository<SpuRegularAttributeValue, Long> {
}
