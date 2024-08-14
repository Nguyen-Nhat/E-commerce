package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuSaleAttributeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 21:09:01.837
 */

@Repository
public interface SpuSaleAttributeMappingRepository extends JpaRepository<SpuSaleAttributeMapping, Long> {
    Optional<SpuSaleAttributeMapping> findBySpuIdAndAttributeNameId(Long spuId, Long attributeNameId);
}
