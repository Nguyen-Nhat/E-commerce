package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuSaleAttributeMapping;
import org.ckxnhat.resource.model.product.SpuSaleAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:10:31.839
 */

@Repository
public interface SpuSaleAttributeValueRepository extends JpaRepository<SpuSaleAttributeValue, Long> {
    @Query("SELECT s FROM SpuSaleAttributeValue s WHERE s.spuSaleAttributeMapping.spu.id = ?1")
    List<SpuSaleAttributeValue> findAllBySpuId(Long spuId);
}
