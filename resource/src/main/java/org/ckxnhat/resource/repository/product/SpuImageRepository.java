package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-11 09:22:07.321
 */

@Repository
public interface SpuImageRepository extends JpaRepository<SpuImage, Long> {
    List<SpuImage> findAllBySpuIdAndIdIn(Long id, List<Long> ids);
    void deleteBySpuIdAndIdIn(Long id, List<Long> ids);
    List<SpuImage> findAllBySpuId(Long id);
}
