package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:13:04.759
 */

@Repository
public interface SpuDescriptionRepository extends JpaRepository<SpuDescription, Long> {
    List<SpuDescription> findAllBySpuIdAndIdIn(Long spuId, List<Long> ids);
    void deleteBySpuIdAndIdIn(Long id, List<Long> ids);
    List<SpuDescription> findAllBySpuId(Long spuId);
}
