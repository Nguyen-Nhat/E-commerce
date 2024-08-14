package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.SpuSaleAttributeCombination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-08-05 21:09:50.831
 */

@Repository
public interface SpuSaleAttributeCombinationRepository extends JpaRepository<SpuSaleAttributeCombination, Long> {
    Page<SpuSaleAttributeCombination> findAllBySpuId(Long spuId, Pageable pageable);
}
