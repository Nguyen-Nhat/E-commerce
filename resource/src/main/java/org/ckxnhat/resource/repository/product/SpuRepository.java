package org.ckxnhat.resource.repository.product;

import org.ckxnhat.resource.model.product.Spu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MinhNhat
 * @email nguyennhat.110120@gmail.com
 * @datetime 2024-07-19 17:12:12.504
 */

@Repository
public interface SpuRepository extends JpaRepository<Spu, Long> {
}
