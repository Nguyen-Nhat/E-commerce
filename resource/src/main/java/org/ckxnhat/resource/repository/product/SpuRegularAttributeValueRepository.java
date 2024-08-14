package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuRegularAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:27:35.606
 */

@Repository
public interface SpuRegularAttributeValueRepository extends JpaRepository<SpuRegularAttributeValue, Long> {
    List<SpuRegularAttributeValue> findAllBySpuId(Long spuId);
    boolean existsBySpuIdAndAttributeNameId(Long spuId, Long attributeNameId);
    Optional<SpuRegularAttributeValue> findBySpuIdAndAttributeNameId(Long spuId, Long attributeNameId);
    void deleteBySpuIdAndAttributeNameIdIn(Long spuId, List<Long> attributeNameIds);
}
